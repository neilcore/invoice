package core.hubby.backend.business.services;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.controller.dto.CreateInvoiceRequest;
import core.hubby.backend.business.controller.dto.InvoiceTaxEligibility;
import core.hubby.backend.business.entities.Invoice;
import core.hubby.backend.business.entities.LineItems;
import core.hubby.backend.business.repositories.InvoiceRepository;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.services.ContactService;
import core.hubby.backend.tax.entities.TaxType;
import core.hubby.backend.tax.repositories.TaxTypeRepository;
import jakarta.persistence.EntityNotFoundException;
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
	private final InvoiceRepository invoiceRepository;
	private final ContactService contactService;
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
	 * If obj is null, return a new invoice object.
	 * If obj is instance of {@linkplain java.util.UUID} fetch invoice object
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
		invoice.setStatus(request.status().isEmpty() ? InvoiceRepository.INVOICE_STATUS_DRAFT : request.status()); // "DRAFT" is the default status
		invoice.setReference(request.reference());
		
		save(invoice);
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
					 * Check if line item discount rate is specified.
					 * If not, check if customer's default discount rate is specified.
					 */
					Integer discountRateIfExists = lineItem.discountRate() != null
							|| lineItem.discountRate() != 0 ?
							lineItem.discountRate() : customerDefaultDiscount != null ?
									customerDefaultDiscount : null;
					
					Double calculateLineAmount = null;
					calculateLineAmount = discountRateIfExists != null ?
							lineItem.quantity() * lineItem.unitAmount() * ((100 - discountRateIfExists) / 100)
							: lineItem.quantity() * lineItem.unitAmount();
					
					if (calculateLineAmount != null) {
						createLineItem.setDiscountRate(discountRateIfExists);
					}

					createLineItem.setDescription(lineItem.description());
					createLineItem.setQuantity(lineItem.quantity());
					createLineItem.setUnitAmount(lineItem.unitAmount());
					createLineItem.setLineAmount(calculateLineAmount);
					createLineItem.setAccountCode(lineItem.accountCode());
					
					/**
					 * Set tax type
					 * Used as an override if the default Tax Code for the
					 * selected AccountCode is not correct.
					 */
//					TaxType taxType = taxTypeRepository.findById(lineItem.taxType())
//							.orElseThrow(() -> new EntityNotFoundException("TaxType object not found."));
//					createLineItem.setTaxType(taxType);
					
					
					return createLineItem;
				})
				.collect(Collectors.toSet());
		
		invoice.setLineItems(lineItems);
		
		return invoice;
	}
	private void calculateTaxAmount() {
		
	}
	
	/**
	 * This method will calculate the invoice's subTotal,
	 * totalTax, and grandTotal
	 * @param invoice - accepts {@linkplain Invoice} object type
	 * @return - returns modified {@linkplain Invoice} object
	 */
	private Invoice calculateInvoice(Invoice invoice) {
		Double subTotal = invoice.getLineItems().stream()
				.mapToDouble(lineAmount -> lineAmount.getLineAmount())
				.sum();
		
		invoice.setSubTotal(subTotal);
		
		// TODO need to add totalTax here
		// TODO need to add grandTotal here
		return invoice;
	}
	
	// TODO - work on setting ledger account
	private Invoice setLineItemLedgerAccount(Invoice invoice) {
		return invoice;
	}
	
}
