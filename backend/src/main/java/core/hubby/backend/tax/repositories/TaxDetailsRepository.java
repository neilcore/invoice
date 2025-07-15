package core.hubby.backend.tax.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.tax.entities.TaxDetails;

@Repository
public interface TaxDetailsRepository extends JpaRepository<TaxDetails, UUID> {
	// Default sales tax
	static final String DEFAULT_SALES_TAX_EXCLUSIVE = "EXCLUSIVE";
	static final String DEFAULT_SALES_TAX_INCLUSIVE = "INCLUSIVE";
	static final String DEFAULT_SALES_TAX_NO_TAX = "NO_TAX";
	
	// Sales tax period
	static final String SALES_TAX_PERIOD_MONTHLY = "MONTHLY";
	static final String SALES_TAX_PERIOD_BI_MONTHLY = "BI_MONTHLY";
	static final String SALES_TAX_PERIOD_QUARTERLY = "QUARTERLY";
	static final String SALES_TAX_PERIOD_ANNUALLY = "ANNUALLY";
	static final String SALES_TAX_PERIOD_ONE_MONTHLY = "ONE_MONTHLY";
	static final String SALES_TAX_PERIOD_TWO_MONTHLY = "TWO_MONTHLY";
	static final String SALES_TAX_PERIOD_FOUR_MONTHLY = "FOUR_MONTHLY";
	static final String SALES_TAX_PERIOD_SIX_MONTHLY = "SIX_MONTHLY";
	
}
