package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.Invoice;
import core.hubby.backend.core.data.BaseJpaRepository;

@Repository
public interface InvoiceRepository extends BaseJpaRepository<Invoice, UUID> {	
	// Invoice Statuses
	static final String INVOICE_STATUS_DRAFT = "DRAFT"; // The default status
	static final String INVOICE_STATUS_SUBMITTED = "SUBMITTED";
	static final String INVOICE_STATUS_AUTHORISED = "AUTHORISED";
	
	// Invoice Line amount types
	static final String INVOICE_LINE_AMOUNT_TYPE_EXCLUSIVE = "Exclusive";
	static final String INVOICE_LINE_AMOUNT_TYPE_INCLUSIVE = "Inclusive";
	static final String INVOICE_LINE_AMOUNT_TYPE_NOTAX = "NoTax";
	
	// Invoice tax calculation types
	static final String INVOICE_TAX_CALCULATION_TYPE_TAXCALC_AUTO = "TAXCALC/AUTO";

}
