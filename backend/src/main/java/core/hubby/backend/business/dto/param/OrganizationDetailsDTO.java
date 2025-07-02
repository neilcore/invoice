package core.hubby.backend.business.dto.param;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import core.hubby.backend.business.entities.embedded.BillAndSalesPaymentTermElement;
import core.hubby.backend.business.entities.embedded.ExternalLinks;
import core.hubby.backend.business.entities.embedded.TaxDetails;
import core.hubby.backend.core.dto.PhoneDetail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrganizationDetailsDTO(
		Set<InviteUser> inviteOtherUser,
		@NotBlank(message = "Organization name component cannot be blank.")
		String name,
		@NotBlank(message = "Organization legalName cannot be blank.")
		String legalName,
		String tradingName,
		@NotNull(message = "Organization type component cannot be null.")
		UUID organizationType,
		@NotBlank(message = "Organization countryCode component cannot be blank.")
		@Size(min = 2, max = 2)
		String countryCode,
		LinkedHashSet<PhoneDetail> phoneNo,
		@Email(message = "Invalid organization email.")
		String email,
		Set<Map<String, String>> address,
		TaxDetails taxDetails,
		Map<String, BillAndSalesPaymentTermElement> paymentTerms,
		AddFinancialSetting financialSettings,
		Set<ExternalLinks> externalLinks
) {

	/**
	 * Nested record class for handling invited users to organization
	 */
	public record InviteUser(
			@NotNull(message = "userId component cannot be null") UUID userId, 
			@NotBlank(message = "role component cannot be blank.") String role
	) {
	}
	
	/**
	 * Nested record class for handling financial setting details
	 */
	public record AddFinancialSetting(
			@NotBlank(message = "defaultCurrency component cannot be blank") String defaultCurrency,
			@NotBlank(message = "timeZone component cannot be blank.") String timeZone
	) {
	}
 }
