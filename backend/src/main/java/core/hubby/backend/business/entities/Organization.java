package core.hubby.backend.business.entities;

import java.io.Serializable;
import java.security.PrivateKey;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import core.hubby.backend.business.entities.embedded.ExternalLinks;
import core.hubby.backend.business.entities.embedded.FinancialSettings;
import core.hubby.backend.business.entities.embedded.OrganizationUserInvites;
import core.hubby.backend.business.entities.embedded.OrganizationUsers;
import core.hubby.backend.business.entities.embedded.TaxDetails;
import core.hubby.backend.business.enums.Status;
import core.hubby.backend.core.audit.CreatedDate;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "organization", schema = "app_sc")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@ToString
@Builder
public class Organization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "organization")
	private OrganizationNameUpdate organizationNameUpdate;
	
	@ElementCollection
	@CollectionTable(
			indexes = @Index(name = "idx_one_user_subscriber", columnList = "userId", unique = true),
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
	
	@Column(nullable = false)
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	// The official legal name of the business
	@Column(name = "legal_name", nullable = false, unique = true)
	@NotBlank(message = "Legal name is mandatory")
	private String legalName;
	
	// Optional: The name the business trades under, if different from legal name
	@Column(name = "trading_name")
	private String tradingName;
	
	// e.g., "US", "AU", "NZ", "GB"
	@Column(nullable = false)
	@NotBlank(message = "Country is mandatory")
	private String country;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_type", nullable = false)
	@NotNull(message = "The type where the organization belong to is required")
	private OrganizationType organizationType;
	
	/* Contact Details */	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "phone_no", nullable = false)
	@NotNull(message = "Organization phone number is required")
	@Type(JsonType.class)
	private Map<String, String> phoneNo;
	
	@Column(name = "email", nullable = false)
	@NotBlank(message = "Email is mandatory")
	@Email(message = "Email should be valid")
	private String email;
	
	@ToString.Exclude
	private String website;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "taxIdNo", column = @Column(name = "tax_id_no", nullable = false)),
		@AttributeOverride(name = "taxBasis", column = @Column(name = "tax_basis")),
		@AttributeOverride(name = "taxPeriod", column = @Column(name = "tax_period"))
	})
	private TaxDetails taxDetails;
	
	@ToString.Exclude
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "defaultCurrency", column = @Column(name = "default_currency", nullable = false)),
		@AttributeOverride(name = "timeZone", column = @Column(name = "time_zone", nullable = false))
	})
	private FinancialSettings financialSettings;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Type(JsonType.class)
	@Column(name = "address", nullable = false)
	@NotNull(message = "Organization address cannot be null")
	private Map<String, String> address;
	
	@ToString.Exclude
	@ElementCollection
	@CollectionTable(
			name = "external_links",
			joinColumns = @JoinColumn(name = "organization_id", nullable = false, referencedColumnName = "id")
	)
	private Set<ExternalLinks> externalLinks;
	
	@Type(JsonType.class)
	@Column(name = "payment_terms", nullable = false)
	@NotNull(message = "Organization payment terms cannot be null")
	private Map<String, String> paymentTerms;
	
	@Embedded
	@AttributeOverride(name = "createdDate", column = @Column(name = "created_date", nullable = false))
	private CreatedDate createdDate;
	
	// By default when an organization is created, it has an active subscription
	@Column(name = "active_subscription", nullable = false)
	@Builder.Default
	private boolean activeSubscription = true;
	
	@Column(name = "status", nullable = false)
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private Status status = Status.ACTIVE_ACCOUNT;
	
}
