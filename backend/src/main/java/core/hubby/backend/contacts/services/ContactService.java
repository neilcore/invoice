package core.hubby.backend.contacts.services;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import core.hubby.backend.contacts.dto.param.CreateContactParam;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.repositories.ContactRepository;
import core.hubby.backend.core.embedded.PhoneDetails;
import core.hubby.backend.core.service.phone.ProxyPhoneService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {
	private final ContactRepository contactRepository;
	private final ProxyPhoneService proxyPhoneService;
	
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
	
	public void createContact(CreateContactParam request) {
		Contact newContact = new Contact();
		
		// name details
		newContact.setName(request.name().name());
		newContact.setFirstName(request.name().firstName());
		newContact.setLastName(request.name().lastName());
		
		newContact.setIsSupplier(request.name().isSupplier());
		newContact.setIsCustomer(request.name().isCustomer());
		
		// contact details
		newContact.setEmailAddress(request.contactDetails().emailAddress());
		
		if (!request.contactDetails().phone().isEmpty()) {
			// Validate phone numbers
			LinkedHashSet<PhoneDetails> validatePhoneDetails =
					proxyPhoneService.validatePhones(request.contactDetails().phone(), Optional.empty());
			newContact.setPhoneNo(validatePhoneDetails);
		}
		
		newContact.setCompanyNumber(request.contactDetails().companyNumber());
		
	}

}
