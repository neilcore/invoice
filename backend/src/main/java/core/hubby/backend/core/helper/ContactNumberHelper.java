package core.hubby.backend.core.helper;

import java.time.chrono.IsoChronology;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import core.hubby.backend.core.dto.PhoneDetail;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContactNumberHelper {
	private final ObjectMapper objectMapper;
	private final CountriesApiHelper countriesApiHelper;
	private static final Set<String> phoneTypes = new HashSet<>();
	private static PhoneNumberUtil UTIL = null;
	
	static {
		UTIL =  PhoneNumberUtil.getInstance();
		
		phoneTypes.add("DEFAULT");
		phoneTypes.add("DDI");
		phoneTypes.add("MOBILE");
		phoneTypes.add("FAX");
	}
	
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
	
	private PhoneNumber parsePhoneNumber(String phoneNo, String country) {
		// Validate the country
		if (!countriesApiHelper.getTwoLetterCountryCode().contains(country)) {
			throw new IllegalArgumentException("Country code " + country + " cannot be found");
		}
		
		return transformPhoneNumber(phoneNo, country);
	}
	
	/**
	 * This method is used to parse phone numbers
	 * @param phones
	 * @return - a Set<PhoneDetail> in a string format
	 */
	public String parsePhoneNumbers(Set<PhoneDetail> phones) {
		Set<PhoneDetail> validatedPhones = new HashSet<>();
		
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
	 * This method will map json in string format to
	 * Set<PhoneDetail> format.
	 * @param phonesJsonString - Map type that contains a single node called "phones"
	 * @return - will return a Set<PhoneDetail> object
	 */
	public Set<PhoneDetail> mapPhoneJsonStringToPhoneDetailObject(Map<String, String> phonesJsonString) {
		Set<PhoneDetail> phoneDetails = null;
		String phoneSetStringJson = phonesJsonString.get("phones");
		
		try {
			phoneDetails = objectMapper.readValue(
					phoneSetStringJson,
					new TypeReference<Set<PhoneDetail>>() {}
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return phoneDetails;
	}
}
