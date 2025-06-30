package core.hubby.backend.business.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import core.hubby.backend.business.dto.param.CreateInvoiceDTO;
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
	
	@Transactional
	public InvoiceVO createNewInvoice(CreateInvoiceDTO data) {
		// Get invoice type
		InvoiceType invoiceType = invoiceTypeRepository.findById(data.invoiceType())
				.orElseThrow(() -> new IllegalArgumentException("Invoice type not found"));
		
		// Validate contact
		boolean isUUID = isUuid(data.contact());
		Optional<Contact> contact;
		if (isUUID) {
			contact = contactRepository.findById(UUID.fromString(data.contact()));
		} else {
			CreateContactParam contactParam = new CreateContactParam(data.contact());
			
			UUID newContactId = contactService.createNewContact(contactParam).contactId();
			contact = contactRepository.findById(newContactId);
		}
		
		// Create the invoice first before the LineItems
		Invoice createInvoice = Invoice.builder()
				.invoiceType(invoiceType)
				.contact(contact.get())
				.reference(data.reference())
				.status(data.status())
				.build();
		Invoice newInvoice = invoiceRepository.save(createInvoice);
		// Create the LineItems
		for (Map<String, Object> lineItem: data.lineItems()) {
			try {
				Optional<TaxType> taxtType = taxTypeRepository.findById(UUID.fromString((String) lineItem.get("taxType")));
				if (taxtType.isPresent()) {
					LineItems newLineItem = LineItems.builder()
							.invoice(newInvoice)
							.description((String) lineItem.get("description"))
							.quantity((Double) lineItem.get("quantity"))
							.unitAmount((Double) lineItem.get("unitAmount"))
							.accountCode((String) lineItem.get("accountCode"))
							.taxType(taxtType.get())
							.lineAmountType(data.lineAmountType())
							.build();
					
					lineItemRepository.save(newLineItem);
				}
			} catch (Exception e) {
				throw new RuntimeException("Error saving line item: " + e.getMessage(), e);
			}
		}
		
		List<Map<String, String>> invoices = new ArrayList<>();
		Map<String, Object> invoiceElements = new HashMap<>();
		
		invoiceElements.put("Type", newInvoice.getInvoiceType().getName());
		
		// Get the contact details included in the invoice response
		Map<String, Object> contactDetails = new HashMap<>();
		contactDetails.put("ContactID", newInvoice.getContact().getId().toString());
		contactDetails.put("ContactStatus", newInvoice.getContact().getContactStatus());
		contactDetails.put("Name", newInvoice.getContact().getName());
		
		
		invoiceElements.put("Contact", newInvoice.getContact().getName());
		return null;
	}
	
    /**
     * Checks if a given String is a valid UUID.
     *
     * @param uuidString The string to check.
     * @return true if the string is a valid UUID, false otherwise.
     */
    private static boolean isUuid(String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
	
}
