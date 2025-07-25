package core.hubby.backend.tax.entities;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import core.hubby.backend.business.entities.Organization;
import core.hubby.backend.tax.entities.embedded.TaxNumber;
import core.hubby.backend.tax.repositories.TaxDetailsRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	@JoinColumn(name = "organization", nullable = false, unique = true, referencedColumnName = "id")
	private Organization organization;
	
	// applies to PURCHASES (bills, spend money, etc. – where your organization is buying something).
	// If LineAmountTypes are not specified or provided when creating a bill,
	// The system will use the organization's DefaultPurchasesTax.
	// setting as a fallback to determine if the line item amounts are tax
	// inclusive, exclusive, or none.
	@Column(name = "default_tax_purchases")
	private String defaultTaxPurchases;
	
	@Column(name = "pays_tax")
	private Boolean paysTax;
	
	/**
	 * TODO - needs to gather more info in Xero organization's TaxNumber
	 * Shown if set. Displays in  UI as Tax File Number (AU), TIN (PH),
	 * GST Number (NZ), VAT Number (UK) and Tax ID Number (US & Global).
	 */
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "tax_number", columnDefinition = "jsonb")
	private TaxNumber taxNumber;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "sales_tax_basis", referencedColumnName = "id")
	private SalesTaxBasis salesTaxBasis;
	
	@Column(name = "tax_period")
	private String taxPeriod;
	
	@Column(name = "sales_tax")
	private String defaultSalesTax;
	
	public void setDefaultTaxPurchases(String defaultTaxPurchase) {
		Set<String> taxPurchases = Set.of(
				TaxDetailsRepository.TAX_TYPE_APPLIED_EXCLUSIVE,
				TaxDetailsRepository.TAX_TYPE_APPLIED_INCLUSIVE,
				TaxDetailsRepository.TAX_TYPE_APPLIED_NO_TAX
		);
		if (!defaultTaxPurchase.isBlank()) {
			if (!taxPurchases.contains(defaultTaxPurchase.toUpperCase())) {
				throw new NoSuchElementException("Provided default tax purchase not found.");
			}
			this.defaultSalesTax = defaultTaxPurchase;
		}else {
			this.defaultSalesTax = defaultTaxPurchase;
		}
	}
	
	public void setTaxPeriod(String taxPeriod) {
		Set<String> taxPeriods = Set.of(
				TaxDetailsRepository.SALES_TAX_PERIOD_ANNUALLY,
				TaxDetailsRepository.SALES_TAX_PERIOD_BI_MONTHLY,
				TaxDetailsRepository.SALES_TAX_PERIOD_FOUR_MONTHLY,
				TaxDetailsRepository.SALES_TAX_PERIOD_MONTHLY,
				TaxDetailsRepository.SALES_TAX_PERIOD_ONE_MONTHLY,
				TaxDetailsRepository.SALES_TAX_PERIOD_QUARTERLY,
				TaxDetailsRepository.SALES_TAX_PERIOD_SIX_MONTHLY,
				TaxDetailsRepository.SALES_TAX_PERIOD_TWO_MONTHLY
		);
		
		if (!taxPeriod.isBlank()) {
			if (!taxPeriods.contains(taxPeriod.toUpperCase())) {
				throw new NoSuchElementException("Provided tax period not found.");
			}
			this.taxPeriod = taxPeriod;
		} else {
			this.taxPeriod = taxPeriod;
		}
	}
}
