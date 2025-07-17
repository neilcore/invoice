package core.hubby.backend.tax.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.hubby.backend.tax.entities.TaxType;

@Repository
public interface TaxTypeRepository extends JpaRepository<TaxType, UUID> {
	// Tax types
	static final String TAX_TYPE_OUTPUT = "OUTPUT";
	static final String TAX_TYPE_INPUT = "INPUT";
	static final String TAX_TYPE_NONE = "NONE";
	static final String TAX_TYPE_BASEEXCLUDED = "BASEEXCLUDED";
	static final String TAX_TYPE_GSTONIMPORTS = "GSTONIMPORTS";
	
}
