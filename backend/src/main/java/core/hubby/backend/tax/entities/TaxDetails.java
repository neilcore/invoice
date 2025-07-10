package core.hubby.backend.tax.entities;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import core.hubby.backend.business.entities.Organization;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO create organization_tax_details migration script
 * Organization's tax details
 */
@Entity
@Table(name = "organization_tax_details", schema = "app_sc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, unique = true)
	private UUID id;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Organization.class)
	@JoinColumn(name = "organization", nullable = false, unique = true)
	private Organization organization;
	
	// applies to PURCHASES (bills, spend money, etc. – where your organization is buying something).
	// If LineAmountTypes are not specified or provided when creating a bill,
	// The system will use the organization's DefaultPurchasesTax.
	// setting as a fallback to determine if the line item amounts are tax
	// inclusive, exclusive, or none.
	@Column(name = "default_tax_purchases")
	private String defaultTaxPurchases;
	
	@Column(name = "pays_tax")
	private boolean paysTax;
	
	/**
	 * TODO - needs to gather more info in Xero organization's TaxNumber
	 * Shown if set. Displays in  UI as Tax File Number (AU), TIN (PH),
	 * GST Number (NZ), VAT Number (UK) and Tax ID Number (US & Global).
	 */
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "tax_number", columnDefinition = "jsonb")
	private Map<String, String> taxNumber;
	
	@Column(name = "tax_basis")
	private String taxBasis;
	
	@Column(name = "tax_period")
	private String taxPeriod;
	
	@Column(name = "sales_tax")
	private String salesTax;
	
	public void setTaxNumber(Map<String, String> taxNumber) {
		this.taxNumber = this.paysTax ? taxNumber : null;
	}
}
