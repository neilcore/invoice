package core.hubby.backend.core.service.phone;

import java.util.LinkedHashSet;
import java.util.Optional;

import org.springframework.stereotype.Service;

import core.hubby.backend.core.embedded.PhoneDetails;
import lombok.RequiredArgsConstructor;

/**
 * In this phone service. country codes on every phone details
 * are required and must be checked before proceeding.
 */
@Service
@RequiredArgsConstructor
public class ProxyPhoneService implements PhoneServiceInterface {
	private final PhoneService phoneService;
	
	@Override
	public LinkedHashSet<PhoneDetails> validatePhones(LinkedHashSet<PhoneDetails> phones,
			Optional<String> countryCode) {
		// Validate phone numbers
		// For contacts phone country code cannot be null.
		phones.stream()
		.forEach(pn -> {
			if(pn.getPhoneCountryCode().isBlank()) {
				throw new IllegalArgumentException("Phone country code cannot be null.");
			}
		});
		return phoneService.validatePhones(phones, Optional.empty());
	}

}
