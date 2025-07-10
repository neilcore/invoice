package core.hubby.backend.contacts.services;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import core.hubby.backend.contacts.dto.param.CreateContactParam;
import core.hubby.backend.contacts.dto.vo.ContactVO;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.entities.PaymentTerms;
import core.hubby.backend.contacts.repositories.ContactRepository;
import core.hubby.backend.contacts.repositories.PaymentTermsRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {
	private final ContactRepository contactRepository;
	private final PaymentTermsRepository paymentTermsRepository;
	
	/**
	 * This method will retrieve or create new contact if it can't find one.
	 * If obj is instance of {@linkplain java.util.String} then create new contact entity
	 * only by name.
	 * If obj is instance of {@linkplain java.util.UUID} find the contact object using it's
	 * ID.
	 * @param obj - accepts {@linkplain Object} type
	 * @return - {@linkplain java.util.Optional} object that can hold {@linkplain Contact} object type.
	 * @throws - NoSuchElementException if it can't find the contact object by
	 * it's given ID.
	 */
	public Optional<Contact> findOrCreate(@NotNull Map<String, Object> contact) {
		Optional<Contact> getContact = Optional.empty();
		Object obj = contact.get("contactId");
		
		if(obj instanceof String name) {
			Contact createContactByName = Contact
					.builder()
					.name(name)
					.build();
			getContact = Optional.of(contactRepository.save(createContactByName));
		} else if(obj instanceof UUID id) {
			Optional<Contact> findContact = contactRepository
					.findById(id);
			
			if(findContact.isEmpty()) {
				throw new NoSuchElementException("Cannot find countact object.");
			} else {
				getContact = findContact;
			}
		} else {
			throw new IllegalArgumentException("Invalid method argument.");
		}
		
		return getContact;
	}
	
	public ContactVO createNewContact(CreateContactParam data) {
		// Check contact details
		filterContactDetails(data.address(), "ADDRESS");
		filterContactDetails(data.phone(), "PHONE");
		
		Map<String, Object> addressData = new HashMap<>();
		addressData.put("Addresses", data.address());
		
		Map<String, Object> phones = new HashMap<>();
		phones.put("Phones", data.phone());
		
		// Get payment terms entity
		PaymentTerms paymentTerms = null;
		if (data.paymentTerms() != null) {
			paymentTerms = paymentTermsRepository.findById(data.paymentTerms())
					.orElseThrow(() -> new IllegalArgumentException("Payment terms not found"));
		}
		
		Contact contact = Contact.builder()
				.name(data.name())
				.firstName(data.firstName())
				.lastName(data.lastName())
				.emailAddress(data.emailAddress())
				.contactNumber(data.contactNumber())
				.accountNumber(data.accountNumber())
				.companyNumber(data.companyNumber())
				.taxNumber(data.taxNumber())
				.isSupplier(data.isSupplier() != null ? data.isSupplier() : false)
				.isCustomer(data.isCustomer() != null ? data.isCustomer() : false)
				.address(addressData)
				.phone(phones)
				.paymentTerms(paymentTerms)
				.build();
		
		Contact newContact = contactRepository.save(contact);
		
		Map<String, Object> addresses = new HashMap<>();
		addresses.put("Addresses", newContact.getAddress().get("Addresses"));
		System.out.println("Addresses: " + addresses);
		
		return null;
//		return ContactVO.builder()
//				.contactId(newContact.getId())
//				.contactNumber(newContact.getContactNumber())
//				.accountNumber(newContact.getAccountNumber())
//				.contactStatus(newContact.getContactStatus())
//				.name(newContact.getName())
//				.firstName(newContact.getFirstName())
//				.lastName(newContact.getLastName())
//				.emailAddress(newContact.getEmailAddress())
//				.companyNumber(newContact.getCompanyNumber())
//				.taxNumber(newContact.getTaxNumber())
//				.addresses(addresses)
//				.isCustomer(newContact.getIsCustomer())
//				.isSupplier(newContact.getIsSupplier())
//				.build();
	}
	
	private boolean ifContainsElement(String keyword, List<String> keywords) {
		if (!keywords.contains(keyword.toLowerCase())) {
			return false;
		}
		return true;
	}
	private List<Map<String, Object>> filterContactDetails(List<Map<String, Object>> details, String type) {
		if (type == "ADDRESS") {
			List<String> addressKeywords = List.of("addresstype", "addressline", "city", "postalcode", "attentionto");
			for (Map<String, Object> address : details) {
				for (String keyword: address.keySet()) {
					if (!ifContainsElement(keyword, addressKeywords)) {
						address.remove(keyword);
					}
				}
			}
			return details;
		}
		
		List<String> phoneKeywords = List.of("phonenumber", "phonetype", "phoneareacode", "phonecountrycode");
		for (Map<String, Object> phone : details) {
			for (String keyword: phone.keySet()) {
				if (!ifContainsElement(keyword, phoneKeywords)) {
					phone.remove(keyword);
				}
			}
		}
		return details;
	}

}
