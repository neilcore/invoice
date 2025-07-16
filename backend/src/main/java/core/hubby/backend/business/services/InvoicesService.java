package core.hubby.backend.business.services;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import core.hubby.backend.business.controller.dto.CreateInvoiceRequest;
import core.hubby.backend.business.entities.Invoice;
import core.hubby.backend.business.entities.LineItems;
import core.hubby.backend.business.repositories.InvoiceRepository;
import core.hubby.backend.business.repositories.OrganizationRepository;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.services.ContactService;
import core.hubby.backend.tax.repositories.TaxTypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoicesService {
	private final OrganizationRepository organizationRepository;
	private final InvoiceRepository invoiceRepository;
	private final ContactService contactService;
	private final TaxTypeRepository taxTypeRepository;
	
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
					
					Double calculateLineAmount = discountRateIfExists != null ?
							lineItem.quantity() * lineItem.unitAmount() * ((100 - discountRateIfExists) / 100)
							: lineItem.quantity() * lineItem.unitAmount();
					
					createLineItem.setDiscountRate(discountRateIfExists);
					createLineItem.setDescription(lineItem.description());
					createLineItem.setQuantity(lineItem.quantity());
					createLineItem.setUnitAmount(lineItem.unitAmount());
					createLineItem.setLineAmount(calculateLineAmount);
					createLineItem.setAccountCode(lineItem.accountCode());
					createLineItem.setTaxType(taxTypeRepository.findById(lineItem.taxType()).orElseThrow(() -> new NoSuchElementException("Tax type object cannot be found.")));
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
