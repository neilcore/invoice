package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import core.hubby.backend.business.entities.embedded.DefaultCurrency;
import core.hubby.backend.business.entities.embedded.ExternalLinks;
import core.hubby.backend.business.entities.embedded.OrganizationUserInvites;
import core.hubby.backend.business.entities.embedded.OrganizationUsers;
import core.hubby.backend.business.enums.Status;
import core.hubby.backend.core.audit.CreatedDate;
import core.hubby.backend.core.embedded.PhoneDetails;
import core.hubby.backend.tax.entities.TaxDetails;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "organization", schema = "app_sc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "profile_image")
	@NotNull(message = "profile_image")
	private String profileImage;
	
	@ElementCollection
	@CollectionTable(
			schema = "app_sc",
			name = "organization_name_update",
			joinColumns = {
					@JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
			})
	private Set<OrganizationNameUpdate> nameDetailsUpdate;
	
	@ElementCollection
	@CollectionTable(
			schema = "app_sc",
			name = "organization_users",
			joinColumns = {
					@JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = false)
			})
	private Set<OrganizationUsers> organizationUsers;
	
	@ElementCollection
	@CollectionTable(
			schema = "app_sc",
			name = "organization_user_invites",
			joinColumns = {@JoinColumn(name = " organization_id", referencedColumnName = "id", nullable = false)})
	private Set<OrganizationUserInvites> organizationUserInvites;
	
	@Column(name = "display_name", nullable = false, unique = true)
	@NotBlank(message = "displayName attribute cannot be blank.")
	private String displayName;
	
	// The official legal name or trading name of the business
	@Column(name = "legal_name", nullable = false, unique = true)
	@NotBlank(message = "legalName cannot be blank.")
	private String legalName;
	
	@Column(name = "organization_description")
	private String organizationDescription;
	
	// e.g., "US", "AU", "NZ", "GB"
	@Column(nullable = false)
	@NotNull(message = "country attribute cannot be null.")
	private String country;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "default_currency", nullable = false, columnDefinition = "jsonb")
	@NotNull(message = "defaultCurrency attribute cannot be null.")
	private DefaultCurrency defaultCurrency;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_type", nullable = false)
	@NotNull(message = "organizationType attribute cannot be null.")
	private OrganizationType organizationType;
	
	/* Contact Details */
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "phone_no", nullable = false, columnDefinition = "jsonb[]")
	private LinkedHashSet<PhoneDetails> phoneNo;
	
	@Column(name = "email", nullable = false)
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email attribute should be valid")
	private String email;
	
	@ToString.Exclude
	private String website;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "organization")
	private TaxDetails taxDetails;
	
	@NotBlank(message = "timeZone attribute is required.")
	@Column(name = "time_zone")
	private String timeZone;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "financial_year", columnDefinition = "jsonb")
	private Map<String, Integer> financialYear;
	
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "address", nullable = false, columnDefinition = "jsonb[]")
	@NotNull(message = "Address attribute cannot be null.")
	private Set<Map<String, String>> address;
	
	@JdbcTypeCode(SqlTypes.JSON_ARRAY)
	@Column(name = "external_links", columnDefinition = "jsonb[]")
	private Set<ExternalLinks> externalLinks;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "payment_terms")
	@NotNull(message = "paymentTerms attribute cannot be null.")
	private Map<String, String> paymentTerms;
	
	@Embedded
	@AttributeOverride(name = "createdDate", column = @Column(name = "created_date", nullable = false))
	private CreatedDate createdDate;
	
	// By default when an organization is created, it has an active subscription
	@Column(name = "active_subscription", nullable = false)
	private Boolean activeSubscription = true;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE_ACCOUNT;
	
	public void setStatus(Status status) {
		if (status != null) {
			this.status = status;
		} else {
			this.status = Status.ACTIVE_ACCOUNT;
		}
	}
	
	public void setActiveSubscription(Boolean isActiveSubscription) {
		this.activeSubscription = isActiveSubscription == null ?
				true : isActiveSubscription;
	}
	
	public void setBasicInformation(
			String displayName,
			String legalName,
			OrganizationType organizationType,
			String description
	) {
		this.setDisplayName(displayName);
		this.setLegalName(legalName);
		this.setOrganizationType(organizationType);
		this.setOrganizationDescription(description);
	}
	
	public void setContactDetails(
			String countryCode,
			DefaultCurrency defaultCurrency,
			Set<Map<String, String>> address,
			LinkedHashSet<PhoneDetails> phones,
			String email,
			String website,
			Set<ExternalLinks> externalLinks
	) {
		this.setCountry(countryCode);
		this.setDefaultCurrency(defaultCurrency);
		this.setAddress(address);
		this.setPhoneNo(phones);
		this.setEmail(email);
		this.setWebsite(website);
		this.setExternalLinks(externalLinks);
	}
	
}
