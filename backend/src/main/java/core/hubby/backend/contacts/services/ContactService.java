package core.hubby.backend.contacts.services;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import core.hubby.backend.business.entities.PaymentTerms;
import core.hubby.backend.business.repositories.PaymentTermsRepository;
import core.hubby.backend.contacts.dto.param.CreateContactParam;
import core.hubby.backend.contacts.dto.vo.ContactVO;
import core.hubby.backend.contacts.entities.Contact;
import core.hubby.backend.contacts.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {
	private final ContactRepository contactRepository;
	private final PaymentTermsRepository paymentTermsRepository;
	
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
