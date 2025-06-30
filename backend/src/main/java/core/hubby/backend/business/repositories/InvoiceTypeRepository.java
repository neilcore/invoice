package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.InvoiceType;
import core.hubby.backend.core.data.BaseJpaRepository;

@Repository
public interface InvoiceTypeRepository extends BaseJpaRepository<InvoiceType, UUID> {
	// Invoice Types
	static final String INVOICE_TYPE_ACCREC = "ACCREC";
	static final String INVOICE_TYPE_ACCPAY = "ACCPAY";
}
