package core.hubby.backend.core.helper;

import java.security.PublicKey;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import core.hubby.backend.core.dto.PhoneDetail;
import core.hubby.backend.core.exception.CountryNotFoundException;
import jakarta.annotation.PostConstruct;

@Component
public class ContactNumberHelper {
	private final ObjectMapper objectMapper;
	private final CountryService countryService;
	private Set<String> phoneTypes;
	private PhoneNumberUtil UTIL;
	
	public ContactNumberHelper(ObjectMapper mapper, CountryService countryService) {
		this.objectMapper = mapper;
		this.countryService = countryService;
	}
	
	@PostConstruct
	private void init() {
		phoneTypes = Set.of("DEFAULT", "DDI", "MOBILE", "FAX");
		UTIL = PhoneNumberUtil.getInstance();
	}
	
	/**
	 * This method will check the phone type
	 * @param type - {@linkplain java.util.String} object type
	 * @return - boolean
	 */
	private boolean checkPhoneType(String type) {
		if (phoneTypes.contains(type.toUpperCase())) return true;
		return false;
	}
	
	private PhoneNumber transformPhoneNumber(String phoneNo, String country) {
		PhoneNumber parsedPhoneNumber = null;
		
		try {
			parsedPhoneNumber = UTIL.parse(phoneNo, country);
		} catch (NumberParseException e) {
			System.err.println("NumberParseException was thrown: " + e.toString());
		}
		
		return parsedPhoneNumber;
	}
	
	/**
	 * Validate country request
	 * @param phoneNo - accepts {@linkplain java.util.String} object type
	 * @param country - accepts {@linkplain java.util.String} object type
	 * @return - {@linkplain PhoneNumber} object type
	 */
	private PhoneNumber parsePhoneNumber(String phoneNo, String country) {
		if (!countryService.validateCountryCode(country)) {
			throw new CountryNotFoundException(country);
		}
		return transformPhoneNumber(phoneNo, country);
	}
	
	/**
	 * This method is used to parse phone numbers
	 * @param phones
	 * @return - a Set<PhoneDetail> in a string format
	 */
	public String parsePhoneNumbers(LinkedHashSet<PhoneDetail> phones) {
		LinkedHashSet<PhoneDetail> validatedPhones = new LinkedHashSet<>();
		
		for (PhoneDetail phone: phones) {
			// Check if phone type is valid
			if (!checkPhoneType(phone.phoneType())) {
				throw new IllegalArgumentException("Phone type " + phone.phoneType() + " unknown.");
			}
			PhoneNumber parsedPhone = parsePhoneNumber(phone.phoneNumber(), phone.phoneCountryCode());
			validatedPhones.add(
					new PhoneDetail(
							phone.phoneType(),
							String.valueOf(parsedPhone.getNationalNumber()),
							phone.phoneAreaCode().toString(),
							String.valueOf(parsedPhone.getCountryCode()),
							phone.isDefault() ? Boolean.TRUE : Boolean.FALSE
					)
			);
		}
		
		return validatedPhones.toString();
	}
	
	/**
	 * TODO learn about Jackson's TypeReference
	 * This method will map JSON in string format to
	 * {@linkplain java.util.LinkedHashSet} that contains {@linkplain PhoneDetail} objects.
	 * @param jsonStr - {@linkplain java.util.Map} type that contains a single node called "phones".
	 * @return - {@linkplain java.util.LinkedHashSet} that contains {@linkplain PhoneDetail} objects.
	 */
	public LinkedHashSet<PhoneDetail> fromJsonStringToObject(Map<String, String> jsonStr) {
		LinkedHashSet<PhoneDetail> phoneDetails = null;
		String phoneSetStringJson = jsonStr.get("phones");
		
		try {
			phoneDetails = objectMapper.readValue(
					phoneSetStringJson,
					new TypeReference<LinkedHashSet<PhoneDetail>>() {}
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return phoneDetails;
	}
}
