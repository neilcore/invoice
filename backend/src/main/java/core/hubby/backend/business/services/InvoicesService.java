package core.hubby.backend.business.services;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import core.hubby.backend.business.dto.param.CreateInvoiceRequest;
import core.hubby.backend.business.entities.Invoice;
import core.hubby.backend.business.entities.InvoiceType;
import core.hubby.backend.business.entities.LineItems;
import core.hubby.backend.business.repositories.InvoiceRepository;
import core.hubby.backend.business.repositories.InvoiceTypeRepository;
import core.hubby.backend.business.repositories.LineItemRepository;
import core.hubby.backend.business.repositories.TaxTypeRepository;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.repositories.ContactRepository;
import core.hubby.backend.contacts.services.ContactService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoicesService {
	private final InvoiceRepository invoiceRepository;
	private final InvoiceTypeRepository invoiceTypeRepository;
	private final ContactRepository contactRepository;
	private final ContactService contactService;
	private final TaxTypeRepository taxTypeRepository;
	private final LineItemRepository lineItemRepository;
	
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
	
	public void createNewInvoiceObject(CreateInvoiceRequest request) {
		// Create new Invoice object
		Invoice invoice = retrieveInvoiceObject(null);
		
		/**
		 * Retrieve and set invoice type object
		 */
		Optional<InvoiceType> findInvoiceType = invoiceTypeRepository.findById(request.invoiceType());
		InvoiceType getInvoiceType = null;
		
		if(findInvoiceType.isEmpty()) {
			throw new NoSuchElementException("Invoice type object cannot be found");
		} else {
			getInvoiceType = findInvoiceType.get();
		}
		
		invoice.setInvoiceType(getInvoiceType);
		
		// Set invoice contact
		Optional<Contact> getContactObject = contactService.findOrCreate(request.contact());
		if (getContactObject.isEmpty()) {
			throw new NoSuchElementException("Contact object cannot be found.");
		} else {
			invoice.setContact(getContactObject.get());
		}
		
		setLineItems(invoice, request.lineItems(), request.lineAmountType());
		invoice.setDate(request.date());
		invoice.setDueDate(request.dueDate());
		invoice.setStatus(request.status().isEmpty() ? "DRAFT" : request.status()); // "DRAFT" is the default status
		invoice.setReference(request.reference());
		
		save(invoice);
	}
	
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
			String lineAmountTypeRequest
	) {
		/**
		 * In Java, when you use a variable from an outer scope within a
		 * lambda expression or an anonymous inner class, that variable must
		 * be final or "effectively final." A variable is effectively final
		 * if its value is never changed after it is initialized.
		 * In this code, lineAmountType is initialized, but then its value is potentially
		 * reassigned within the if-else if-else block. Even though in any given
		 * execution path it will only be assigned once, the compiler sees the
		 * potential for multiple assignments, making it not effectively final.
		 */
		final String lineAmountType;
		if (Objects.equals(lineAmountTypeRequest, LineItemRepository.LINE_AMOUNT_TYPE_EXCLUSIVE)) {
			lineAmountType = LineItemRepository.LINE_AMOUNT_TYPE_EXCLUSIVE;
		} else if (Objects.equals(lineAmountTypeRequest, LineItemRepository.LINE_AMOUNT_TYPE_INCLUSIVE)) {
			lineAmountType = LineItemRepository.LINE_AMOUNT_TYPE_INCLUSIVE;
		} else {
			lineAmountType = LineItemRepository.LINE_AMOUNT_TYPE_NO_TAX;
		}
		
		Set<LineItems> lineItems = lineItemsSet
				.stream()
				.map( lineItem -> {
					LineItems createLineItem = LineItems.builder()
							.lineAmountType(lineAmountType) // lineAmountType is now effectively final
							.description(lineItem.description())
							.quantity(lineItem.quantity())
							.unitAmount(lineItem.unitAmount())
							.accountCode(lineItem.accountCode())
							.taxType(taxTypeRepository.findById(lineItem.taxType()).orElseThrow(() -> new NoSuchElementException("Tax type object cannot be found.")))
							.build();
					return createLineItem;
				})
				.collect(Collectors.toSet());
		invoice.setLineItems(lineItems);
		
		return invoice;
	}
	
}
