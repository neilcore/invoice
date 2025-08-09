package core.hubby.backend.business.services;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import core.hubby.backend.accounts.repositories.AccountRepository;
import core.hubby.backend.accounts.repositories.projections.AccountLookup;
import core.hubby.backend.business.controller.dto.CreateInvoiceRequest;
import core.hubby.backend.business.controller.dto.InvoiceTaxEligibility;
import core.hubby.backend.business.controller.dto.UpdateInvoiceResponse;
import core.hubby.backend.business.entities.Invoice;
import core.hubby.backend.business.entities.LineItems;
import core.hubby.backend.business.entities.embedded.InvoiceSettings;
import core.hubby.backend.business.repositories.InvoiceRepository;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.business.repositories.OrganizationSettingsRepository;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.services.ContactService;
import core.hubby.backend.tax.repositories.TaxRateRepository;
import core.hubby.backend.tax.repositories.TaxTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * When you select an AccountCode for a line item,
 * that account often has a default TaxType associated with it in Xero.
 * However, you can override this default by explicitly setting TaxType
 * in your API request.
 */
@Service
@RequiredArgsConstructor
public class InvoiceService {

	private final OrganizationRepository organizationRepository;
	private final OrganizationSettingsRepository organizationSettingsRepository;
	private final InvoiceRepository invoiceRepository;
	private final ContactService contactService;
	private final AccountRepository accountRepository;
	private final TaxRateRepository taxRateRepository;
	private final TaxTypeRepository taxTypeRepository;
	
	/**
	 * When the user creates an invoice (e.g. clicks the button for creating
	 * new invoice) a request (GET) will be sent and this will be the response.
	 * This method checks if tax can be applied to the invoice base on the
	 * organization's country code.
	 * @param organizationID - accepts {@linkplain java.util.UUID} object type.
	 * @return - {@linkplain InvoiceTaxEligibility} object type.
	 */
	public InvoiceTaxEligibility taxEligibility(@NotNull UUID organizationID) {
		Optional<String> country = organizationRepository
				.findCountryUsingOrganizationId(organizationID);
		Boolean existsByLabel = taxTypeRepository.existsByLabelIgnoreCase(country.get());
		
		if (existsByLabel) {
			return new InvoiceTaxEligibility(organizationID, existsByLabel, TaxTypeRepository.COUNTRY_ELIGIBLE_FOR_TAX);
		}
		
		return new InvoiceTaxEligibility(
				organizationID,
				existsByLabel,
				TaxTypeRepository.COUNTRY_INELIGIBLE_FOR_TAX
		);
	}
	
	/**
	 * This method will retrieve an invoice object
	 * @param obj - accepts {@linkplain Object} type
	 * If @param obj is null, return a new invoice object.
	 * If @param obj is instance of {@linkplain java.util.UUID} fetch invoice object
	 * using it's ID
	 * @return - {@linkplain Invoice} object type.
	 */
	private Invoice retrieveInvoiceObject(Object obj) {
		Optional<Invoice> findInvoice = Optional.empty();
		if (obj == null) {
			findInvoice = Optional.of(new Invoice());
		} else if (obj instanceof UUID id) {
			findInvoice = invoiceRepository.findById(id);
			if (findInvoice.isEmpty()) {
				throw new NoSuchElementException("Invoice object cannot be found.");
			}
		}
		
		return findInvoice.get();
	}
	
	public void createNewInvoiceObject(CreateInvoiceRequest request, UUID organizationId) {
		// Create new Invoice object
		Invoice invoice = retrieveInvoiceObject(null);
		
		// Set the invoice type
		invoice.setType(request.invoiceType());
		
		// Set invoice contact
		Optional<Contact> getContactObject = contactService.findOrCreate(request.contact());
		getContactObject.ifPresentOrElse(
				obj -> invoice.setContact(obj),
				() -> new NoSuchElementException("Contact object cannot be found.")
		);
		
		setLineItems(
				invoice,
				request.lineItems(),
				request.lineAmountType(),
				getContactObject.get().getDefaultDiscount(),
				organizationId
		);
		calculateInvoice(invoice);
		
		invoice.setDate(request.date());
		invoice.setDueDate(request.dueDate());
		
		/**
		 * If status is not provided in the request.
		 * Use the organization's default invoice status set in organization's settings.
		 */
		Optional<String> status = Optional.empty();
		if (request.status().isBlank()) {
			InvoiceSettings invoiceSettings = organizationSettingsRepository.findSettingsByOrganizationId(organizationId);
			status = Optional.of(invoiceSettings.getStatus());
		}else {
			status = Optional.of(request.status());
		}
		invoice.setStatus(status.get());
		
		// TODO - work the reference
		invoice.setReference(request.reference());
		
		this.save(invoice);
	}
	
	/**
	 * This method will persist the invoice object to the database.
	 * @param invoice - accepts {@linkplain Invoice} object type.
	 */
	private void save(Invoice invoice) {
		invoiceRepository.save(invoice);
	}
	
	/**
	 * This method is used to set the lineAmount type and LineItems of an invoice.
	 * @param invoice - accepts {@linkplain Invoice} object type.
	 * @param lineItemsSet - {@linkplain java.util.Set} object that holds {@linkplain CreateInvoiceRequest.LineItems}
	 * objects.
	 * @param lineAmountTypeRequest - {@linkplain java.util.String} object type.
	 * @return - modified {@linkplain Invoice} object
	 */
	private Invoice setLineItems(
			Invoice invoice,
			Set<CreateInvoiceRequest.LineItems> lineItemsSet,
			String lineAmountTypeRequest,
			Integer customerDefaultDiscount,
			UUID organizationId
	) {
		/**
		 * Set lineAmountTypeRequest.
		 * If lineAmountTypeRequest is not specified then use the organization's
		 * DefaultPurchasesTax (If specified)
		 */
		Optional<String> lineAmountType = Optional.empty();
		Optional<String> organizationDefaultTaxPurchase = 
				organizationRepository.findLineAmountType(organizationId);
		
		// Set line amount type
		if (!lineAmountTypeRequest.isBlank() && !lineAmountTypeRequest.isEmpty()) {
			lineAmountType = Optional.of(lineAmountTypeRequest);
		} else if (lineAmountTypeRequest.isBlank() || lineAmountTypeRequest.isEmpty()) {
			if (organizationDefaultTaxPurchase.isPresent()) {
				lineAmountType = Optional.of(organizationDefaultTaxPurchase.get());
			} else {
				// Type EXCLUSIVE is the default if both lineAmountTypeRequest and 
				// organizationDefaultTaxPurchase is not specified
				lineAmountType = Optional.of(InvoiceRepository.INVOICE_LINE_AMOUNT_TYPE_EXCLUSIVE);
			}
		}
		invoice.setLineAmountTypes(lineAmountType.get());
		
		Set<LineItems> lineItems = lineItemsSet
				.stream()
				.map( lineItem -> {
					LineItems createLineItem = new LineItems();
					
					/**
					 * Set the tax type.
					 * If overrideTaxType() isn't provided, then get tax type by account code.
					 */
					String taxType = null;
					if (lineItem.overrideTaxType().isBlank()) {
						Optional<AccountLookup> accountLookup = accountRepository
								.findAccountByCodeAndOrganization(lineItem.accountCode(), organizationId);
						if (accountLookup.isEmpty()) {
							throw new EntityNotFoundException("Account lookup not found.");
						}
						taxType = accountLookup.get().getAccountCode();
					} else {
						taxType = lineItem.overrideTaxType();
					}

					// Check if line item discount rate is specified.
					// If not, check if customer's default discount rate is specified.
					Integer discountRateIfExists = lineItem.discountRate() != null
							|| lineItem.discountRate() != 0 ?
							lineItem.discountRate() : customerDefaultDiscount != null ?
									customerDefaultDiscount : null;
					
					// Calculate the line amount
					Double calculateLineAmount = null;
					calculateLineAmount = discountRateIfExists != null ?
							lineItem.quantity() * lineItem.unitAmount() * ((100 - discountRateIfExists) / 100)
							: lineItem.quantity() * lineItem.unitAmount();
					
					if (calculateLineAmount != null) {
						createLineItem.setDiscountRate(discountRateIfExists);
					}
					
					/**
					 * Calculate tax amount by:
					 * - calculate net line amount
					 * - get the tax type rate
					 */
					BigDecimal netLineAmount = new BigDecimal(lineItem.quantity() * lineItem.unitAmount());
					BigDecimal effectiveRate = taxRateRepository.findEffectiveRateByOrganziationId(
							organizationId, taxType).get();
					
					BigDecimal calculateTaxAmount = BigDecimal.ZERO;
					BigDecimal lineItemTotal = BigDecimal.ZERO;
					
					if (lineAmountTypeRequest.compareToIgnoreCase(InvoiceRepository.INVOICE_LINE_AMOUNT_TYPE_EXCLUSIVE) == 0) {
						// LineAmountTypes: "Exclusive"
						calculateTaxAmount = netLineAmount.multiply(effectiveRate);
						lineItemTotal = new BigDecimal(lineItem.unitAmount()).add(effectiveRate);
						
					} else if (lineAmountTypeRequest.compareToIgnoreCase(InvoiceRepository.INVOICE_LINE_AMOUNT_TYPE_INCLUSIVE) == 0) {
						// LineAmountTypes: "Inclusive"
						BigDecimal grossPrice = new BigDecimal(lineItem.unitAmount());
						BigDecimal taxRate = effectiveRate.divide(new BigDecimal("100"), 2, RoundingMode.HALF_DOWN);
						
				        // We should first add 1 to the effective rate before dividing.
				        BigDecimal one = new BigDecimal("1");
				        BigDecimal onePlusTaxRate = one.add(taxRate);
						BigDecimal netPrice = grossPrice.divide(onePlusTaxRate);
						calculateTaxAmount = grossPrice.subtract(netPrice);
						lineItemTotal = new BigDecimal(lineItem.unitAmount());
						
					} else { // LineAmountTypes: "NoTax"
						calculateTaxAmount = new BigDecimal("0.00");
						lineItemTotal = new BigDecimal(lineItem.unitAmount());
					}
							
					createLineItem.setDescription(lineItem.description());
					createLineItem.setQuantity(lineItem.quantity());
					createLineItem.setUnitAmount(lineItem.unitAmount());
					createLineItem.setLineAmount(calculateLineAmount);
					createLineItem.setAccountCode(lineItem.accountCode());
					createLineItem.setTaxAmount(calculateTaxAmount);
					createLineItem.setTaxType(taxType);
					createLineItem.setTotal(lineItemTotal);
					
					
					return createLineItem;
				})
				.collect(Collectors.toSet());
		
		invoice.setLineItems(lineItems);
		
		return invoice;
	}
	
	/**
	 * This method will calculate the invoice's subTotal,
	 * totalTax, and grandTotal
	 * @param invoice - accepts {@linkplain Invoice} object type
	 * @return - returns modified {@linkplain Invoice} object
	 */
	private Invoice calculateInvoice(Invoice invoice) {
		/**
		 * This is the sum of all LineAmount values across all LineItems on the invoice,
		 * before any taxes are applied.
		 */
		Double subTotal = invoice.getLineItems().stream()
				.mapToDouble(lineAmount -> lineAmount.getLineAmount())
				.sum();
		
		invoice.setSubTotal(subTotal);
		
		// Calculate and set total tax
		BigDecimal totalTax = BigDecimal.ZERO;
		for (LineItems lt: invoice.getLineItems()) {
			totalTax = totalTax.add(lt.getTaxAmount());
		}
		invoice.setTotalTax(totalTax);
		// Calculate and set grand total
		BigDecimal grandTotal = new BigDecimal(subTotal).add(totalTax);
		invoice.setGrandTotal(grandTotal);
		
		return invoice;
	}
	
	/**
	 * This method will update the invoice's status.
	 * @param invoiceId - accepts {@linkplain java.util.UUID} object type.
	 * @param status - accepts {@linkplain java.util.String} object type.
	 * @return - {@linkplain UpdateInvoiceResponse} object type.
	 */
	@Transactional
	public UpdateInvoiceResponse updateInvoiceStatus(@NotNull UUID invoiceId, @NotNull String status) {
		Invoice findInvoiceById = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new EntityNotFoundException("Invoice not found."));
		String formerStatus = findInvoiceById.getStatus();
		
		findInvoiceById.setStatus(status);
		invoiceRepository.save(findInvoiceById);
		
		String currentStatus = findInvoiceById.getStatus();
		
		return new UpdateInvoiceResponse(
				findInvoiceById.getInvoiceId(),
				formerStatus,
				currentStatus
		);
	}
	
}
