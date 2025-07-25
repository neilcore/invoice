package core.hubby.backend.tax.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import core.hubby.backend.tax.entities.TaxDetails;

@Repository
public interface TaxDetailsRepository extends JpaRepository<TaxDetails, UUID> {
	// Tax types
	static final String TAX_TYPE_APPLIED_EXCLUSIVE = "EXCLUSIVE";
	static final String TAX_TYPE_APPLIED_INCLUSIVE = "INCLUSIVE";
	static final String TAX_TYPE_APPLIED_NO_TAX = "NO_TAX";
	
	// Sales tax period
	static final String SALES_TAX_PERIOD_MONTHLY = "MONTHLY";
	static final String SALES_TAX_PERIOD_BI_MONTHLY = "BI_MONTHLY";
	static final String SALES_TAX_PERIOD_QUARTERLY = "QUARTERLY";
	static final String SALES_TAX_PERIOD_ANNUALLY = "ANNUALLY";
	static final String SALES_TAX_PERIOD_ONE_MONTHLY = "ONE_MONTHLY";
	static final String SALES_TAX_PERIOD_TWO_MONTHLY = "TWO_MONTHLY";
	static final String SALES_TAX_PERIOD_FOUR_MONTHLY = "FOUR_MONTHLY";
	static final String SALES_TAX_PERIOD_SIX_MONTHLY = "SIX_MONTHLY";
	
	@Query("SELECT tx FROM TaxDetails tx WHERE tx.organization = :organizationID")
	Optional<TaxDetails> findTaxDetailsByOrganizationID(@Param("organizationID") UUID organizationID);
	
}
