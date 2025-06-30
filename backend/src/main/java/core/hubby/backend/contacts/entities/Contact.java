package core.hubby.backend.contacts.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.annotations.Type;

import core.hubby.backend.business.entities.PaymentTerms;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact implements Serializable {
	/*
	 * Besides attributes with default values, Only the "name" attribute is non optional
	 * This is useful for automatic contact creation when creating invoices
	 * */
	
	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	// Full name of contact / organization
	@Column(name = "name", nullable = false)
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	// These names are OPTIONAL
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_address")
	@Email(message = "Email address must be valid")
	private String emailAddress;
	
	@Column(name = "contact_number")
	@NotBlank(message = "Contact number cannot be blank")
	private String contactNumber;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	// Company registration number
	@Column(name = "company_number")
	private String companyNumber;
	
	// Tax number
	@Column(name = "tax_number")
	private String taxNumber;
	
	@Column(name = "contact_status", nullable = false)
	@Builder.Default
	private String contactStatus = "ACTIVE";
	
	@Column(name = "is_supplier", nullable = false)
	@NotNull(message = "Supplier status cannot be null")
	@Builder.Default
	private Boolean isSupplier = false;
	
	@Column(name = "is_customer", nullable = false)
	@NotNull(message = "Customer status cannot be null")
	@Builder.Default
	private Boolean isCustomer = false;
	
	
	// Only shown in GET response, not in POST/PUT
	@Type(JsonType.class)
	@Column(name = "address")
	private Map<String, Object> address;
	
	// Only shown in GET response, not in POST/PUT
	@Type(JsonType.class)
	@Column(name = "phone")
	private Map<String, Object> phone;
	
	// Returned in GET response, not in POST/PUT
	@ManyToOne(cascade = jakarta.persistence.CascadeType.ALL)
	@JoinColumn(name = "payment_terms", referencedColumnName = "id")
	private PaymentTerms paymentTerms;
	
	// Returned in GET response, not in POST/PUT
	@Column(name = "updated_date_utc")
	@TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
	private Instant updatedDateUTC;
}
