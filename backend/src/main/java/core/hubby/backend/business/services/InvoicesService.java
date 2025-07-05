package core.hubby.backend.business.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import core.hubby.backend.business.dto.param.CreateInvoiceRequest;
import core.hubby.backend.business.dto.vo.InvoiceVO;
import core.hubby.backend.business.entities.Invoice;
import core.hubby.backend.business.entities.InvoiceType;
import core.hubby.backend.business.entities.LineItems;
import core.hubby.backend.business.entities.TaxType;
import core.hubby.backend.business.repositories.InvoiceRepository;
import core.hubby.backend.business.repositories.InvoiceTypeRepository;
import core.hubby.backend.business.repositories.LineItemRepository;
import core.hubby.backend.business.repositories.TaxTypeRepository;
import core.hubby.backend.contacts.dto.param.CreateContactParam;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.repositories.ContactRepository;
import core.hubby.backend.contacts.services.ContactService;
import jakarta.transaction.Transactional;
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
	}
	
}
