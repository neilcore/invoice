package core.hubby.backend.business.dto.vo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.hubby.backend.core.dto.PhoneDetail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record OrganizationVO (
	List<Details> organization,
	@NotEmpty(message = "organizationTypes component cannot be empty")
	List<Map<String, String>> organizationTypes
) {	
	
	/**
	 * Nested record class for organization entity details
	 */
	public record Details(
			Set<OrganizationUsers> users,
			OrganizationName organizationName,
			String country,
			String organizationType,
			Set<PhoneDetail> phoneNo,
			String email,
			String website,
			TaxDetails taxtDetails
	) {
		public Details {
			if (users.isEmpty()) {
				throw new IllegalArgumentException("Organization users cannot be null");
			}
			if (country == null || country.isBlank()) {
				throw new IllegalArgumentException("Country is mandatory");
			}
			if (organizationType == null || organizationType.isBlank()) {
				throw new IllegalArgumentException("Organization type is mandatory");
			}
			if (phoneNo == null) {
				throw new IllegalArgumentException("Phone number is mandatory");
			}
			if (email == null || email.isBlank()) {
				throw new IllegalArgumentException("Email is mandatory");
			}
		}
		
		/**
		 * Nested record class for organization entity name details
		 */
		public record OrganizationName (
				@NotBlank(message = "name component cannot be blank")
				String name,
				@NotBlank(message = "legalName component cannot be blank")
				String legalName,
				String tradingName,
				@NotNull(message = "updatable component cannot be null")
				Boolean isUpdatable,
				@PastOrPresent(message = "Invalid lastUpdated component value")
				LocalDate lastUpdated
				) {}
	}
	
	/**
	 * Nested record class to hold organization tax details
	 */
	public record TaxDetails(String taxIdNo, String taxBasis, String taxPeriod) {
		public TaxDetails {
			if (taxIdNo.isBlank() || taxIdNo.isEmpty()) {
				throw new IllegalArgumentException("taxIdNo component cannot be null.");
			}
		}
	}
	
	/**
	 * Nested record class to hold organization users
	 */
	public record OrganizationUsers(User user, String role, LocalDate userJoined) {
		
		public OrganizationUsers {
			if (user == null) {
				throw new IllegalArgumentException("Organization user component cannot be null.");
			}
			if (role.isBlank() || role.isEmpty()) {
				throw new IllegalArgumentException("Organization user role component cannot be null");
			}
			// NOTE covert LocalDATE to String
			if (userJoined == null) {
				throw new IllegalArgumentException("Organization user userJoined component cannot be null");
			}
		}
		
		// Nested record class >> for users
		public record User(String userId, String firstName, String lastName, String email) {
			public User{
				if (userId == null) {
					throw new IllegalArgumentException("User UserId component cannot be null.");
				}
				if (firstName.isBlank() || firstName.isEmpty()) {
					throw new IllegalArgumentException("User firstName component cannot be null.");
				}
				if( lastName.isBlank() || lastName.isEmpty()) {
					throw new IllegalArgumentException("User lastName component cannot be null.");
				}
			}
		}
	}
}
