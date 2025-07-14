package core.hubby.backend.core.service.phone;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import core.hubby.backend.core.embedded.PhoneDetails;
import core.hubby.backend.core.exception.CountryNotFoundException;
import core.hubby.backend.core.exception.PhoneTypeNotFoundException;
import core.hubby.backend.core.service.CountryService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * This service will validate and transform phone numbers.
 */
@Service
@RequiredArgsConstructor
public class PhoneService implements PhoneServiceInterface {
	private Set<String> phoneTypes;
	private PhoneNumberUtil UTIL;
	
	private final CountryService countryService;
	
	@PostConstruct
	private void init() {
		phoneTypes = Set.of("DEFAULT", "DDI", "MOBILE", "FAX");
		UTIL = PhoneNumberUtil.getInstance();
	}
	
	private Boolean phoneTypeCheck(String type) {
		if (phoneTypes.contains(type.toUpperCase())) return Boolean.TRUE;
		return Boolean.FALSE;
	}

	private PhoneNumber transformPhone(String phoneNo, String country) {
		PhoneNumber parsedPhoneNumber = null;
		
		try {
			parsedPhoneNumber = UTIL.parse(phoneNo, country);
		} catch (NumberParseException e) {
			System.err.println("NumberParseException was thrown: " + e.toString());
		}
		
		return parsedPhoneNumber;
	}

	private PhoneNumber parsePhone(String number, String countryCode) {
		if (!countryService.validateCountryCode(countryCode)) {
			throw new CountryNotFoundException(countryCode);
		}
		return transformPhone(number, countryCode);
	}

	@Override
	public LinkedHashSet<PhoneDetails> validatePhones(LinkedHashSet<PhoneDetails> phones,Optional<String> countryCode) {
		LinkedHashSet<PhoneDetails> validatedPhones = new LinkedHashSet<>();
		Optional<String> phoneCountryCode = countryCode;
		
		/**
		 * Validate first the phone in the collection.
		 * By default the first phone in the collection is the
		 * primary phone or will be set as primary phone.
		 */
		PhoneDetails primaryPhone = phones.removeFirst();
		if (!phoneTypeCheck(primaryPhone.getPhoneType())) {
			throw new PhoneTypeNotFoundException(primaryPhone.getPhoneType());
		}
		
		if (!primaryPhone.getPhoneCountryCode().isBlank()) {
			phoneCountryCode = Optional.of(primaryPhone.getPhoneCountryCode());
		}
		
		PhoneNumber parsedPrimaryPhone = parsePhone(primaryPhone.getPhoneNumber(), phoneCountryCode.get());
		
		validatedPhones.addFirst(new PhoneDetails(
				primaryPhone.getPhoneType(),
				String.valueOf(parsedPrimaryPhone.getNationalNumber()),
				primaryPhone.getPhoneAreaCode(),
				phoneCountryCode.get(),
				Boolean.TRUE
		));
		
		/**
		 * If there are multiple phones provided
		 */
		if (!phones.isEmpty()) {
			for (PhoneDetails rphone: phones) {
				if (!phoneTypeCheck(rphone.getPhoneType())) {
					throw new PhoneTypeNotFoundException(rphone.getPhoneType());
				}
				
				if (!rphone.getPhoneCountryCode().isBlank()) {
					phoneCountryCode = Optional.of(rphone.getPhoneCountryCode());
				}
				
				PhoneNumber parsedPhone = parsePhone(rphone.getPhoneNumber(), phoneCountryCode.get());
				validatedPhones.add(new PhoneDetails(
						rphone.getPhoneType(),
						String.valueOf(parsedPhone.getNationalNumber()),
						rphone.getPhoneAreaCode(),
						phoneCountryCode.get(),
						Boolean.FALSE
				));
			}
		}
		return validatedPhones;
	}

}
