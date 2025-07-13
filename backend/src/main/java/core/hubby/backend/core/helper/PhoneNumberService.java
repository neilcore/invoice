package core.hubby.backend.core.helper;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import core.hubby.backend.business.entities.embedded.PhoneDetails;
import core.hubby.backend.core.exception.CountryNotFoundException;
import core.hubby.backend.core.exception.PhoneTypeNotFoundException;
import jakarta.annotation.PostConstruct;

@Component
public class PhoneNumberService {
	private final CountryService countryService;
	private Set<String> phoneTypes;
	private PhoneNumberUtil UTIL;
	
	public PhoneNumberService(CountryService countryService) {
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
	 * This method will validate provided phones
	 * @param phones - accepts {@linkplain java.util.LinkedHashSet} object that holds
	 * {@linkplain PhoneDetails} objects.
	 * @param organizationCountryCode - accepts {@linkplain java.util.String} object type.
	 * @return - {@linkplain java.util.LinkedHashSet} object that holds validated
	 * {@linkplain PhoneDetails} objects.
	 */
	public LinkedHashSet<PhoneDetails> validatePhoneDetails(
			LinkedHashSet<PhoneDetails> phones,
			String organizationCountryCode
	) {
		LinkedHashSet<PhoneDetails> validatedPhones = new LinkedHashSet<>();
		String phoneCountryCode = null;
		
		/**
		 * Validate first the phone in the collection.
		 * By default the first phone in the collection is the
		 * primary phone or will be set as primary phone.
		 * 
		 * If phone country code is not provided, use the
		 * organization's country code (which is non Nullable)
		 */
		PhoneDetails primaryPhone = phones.removeFirst();
		if (!checkPhoneType(primaryPhone.getPhoneType())) {
			throw new PhoneTypeNotFoundException(primaryPhone.getPhoneType());
		}
		
		phoneCountryCode = primaryPhone.getPhoneCountryCode() != null ? primaryPhone.getPhoneCountryCode()
				: organizationCountryCode;
		
		PhoneNumber parsedPrimaryPhone = parsePhoneNumber(primaryPhone.getPhoneNumber(), phoneCountryCode);
		validatedPhones.addFirst(new PhoneDetails(
				primaryPhone.getPhoneType(),
				String.valueOf(parsedPrimaryPhone.getNationalNumber()),
				primaryPhone.getPhoneAreaCode(),
				phoneCountryCode,
				Boolean.TRUE
		));
		
		phoneCountryCode = null;
		/**
		 * If there are multiple phones provided
		 */
		if (!phones.isEmpty()) {
			for (PhoneDetails rphone: phones) {
				if (!checkPhoneType(rphone.getPhoneType())) {
					throw new PhoneTypeNotFoundException(rphone.getPhoneType());
				}
				
				phoneCountryCode = rphone.getPhoneCountryCode() != null ? rphone.getPhoneCountryCode()
						: organizationCountryCode;
				PhoneNumber parsedPhone = parsePhoneNumber(rphone.getPhoneNumber(), phoneCountryCode);
				validatedPhones.add(new PhoneDetails(
						rphone.getPhoneType(),
						String.valueOf(parsedPhone.getNationalNumber()),
						rphone.getPhoneAreaCode(),
						phoneCountryCode,
						Boolean.FALSE
				));
				
				phoneCountryCode = null;
			}
		}
		
		return validatedPhones;
	}
}
