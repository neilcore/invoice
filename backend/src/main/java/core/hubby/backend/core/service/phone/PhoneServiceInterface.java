package core.hubby.backend.core.service.phone;

import java.util.LinkedHashSet;
import java.util.Optional;
import core.hubby.backend.core.embedded.PhoneDetails;

public sealed interface PhoneServiceInterface permits
PhoneService, ProxyPhoneService {
	LinkedHashSet<PhoneDetails> validatePhones(
			LinkedHashSet<PhoneDetails> phones,
			Optional<String> countryCode
	);
}
