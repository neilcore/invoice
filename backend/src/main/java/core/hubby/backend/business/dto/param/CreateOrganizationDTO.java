package core.hubby.backend.business.dto.param;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import core.hubby.backend.business.entities.embedded.ExternalLinks;
import core.hubby.backend.core.dto.PhoneDetail;
import jakarta.validation.constraints.Email;

public record CreateOrganizationDTO(
		Set<InviteUser> inviteOtherUser,
		String name,
		String legalName,
		String tradingName,
		UUID organizationType,
		String countryCode,
		Set<PhoneDetail> phoneNo,
		@Email(message = "Not a valid email address.") String email,
		Set<Map<String, String>> address,
		AddTaxDetails taxDetails,
		AddFinancialSetting financialSettings,
		Set<ExternalLinks> externalLinks
) {
	public CreateOrganizationDTO {
		if (address.isEmpty()) {
			throw new IllegalArgumentException("Organization address is required.");
		}
		Objects.requireNonNull(name, "Organization name component is required.");
		Objects.requireNonNull(legalName, "Organization legal name component is required.");
		Objects.requireNonNull(organizationType, "Organization component is required.");
		Objects.requireNonNull(countryCode, "Country code component is required.");
		Objects.requireNonNull(phoneNo, "Organization phone number is required");
		Objects.requireNonNull(email, "Organization email is required");
		Objects.requireNonNull(address, "Organization address is required");
	}
	
	// Nested record class >> inviting other users
	public record InviteUser(UUID userId, String role) {
		public InviteUser {
			if (userId == null) {
				throw new IllegalArgumentException("userId component cannot be null.");
			}
			if (role.isBlank() || role.isEmpty()) {
				throw new IllegalArgumentException("role component cannot be null");
			}
		}
	}
	
	// Nested record class: adding financial setting info
	public record AddFinancialSetting(String defaultCurrency, String timeZone) {
		public AddFinancialSetting {
			if (defaultCurrency.isBlank() || defaultCurrency.isEmpty()) {
				throw new IllegalArgumentException("defaultCurrency component cannot be null.");
			}
			if (timeZone.isBlank() || timeZone.isEmpty()) {
				throw new IllegalArgumentException("timeZone component cannot be null.");				
			}
		}
	}
	
	// Nested record class: Add tax details
	public record AddTaxDetails(String taxIdNo, String taxBasis, String taxPeriod) {
		public AddTaxDetails {
			if (taxIdNo.isBlank() || taxIdNo.isEmpty()) {
				throw new IllegalArgumentException("taxIdNo component cannot be null");
			}
		}
	}
 }
