package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.TaxType;
import core.hubby.backend.core.data.BaseJpaRepository;

@Repository
public interface TaxTypeRepository extends BaseJpaRepository<TaxType, UUID> {
	// Tax types
	static final String TAX_TYPE_INPUT = "INPUT"; // Tax on Purchases
	static final String TAX_TYPE_NONE = "NONE"; // Tax Exempt
	static final String TAX_TYPE_SALES_TAX = "SALES_TAX"; // Tax on Sales
	static final String TAX_TYPE_GSTONIMPORTS = "GSTONIMPORTS"; // Sales Tax on Imports

}
