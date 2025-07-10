package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {	
	// Invoice Statuses
	static final String INVOICE_STATUS_DRAFT = "DRAFT"; // The default status
	static final String INVOICE_STATUS_SUBMITTED = "SUBMITTED";
	static final String INVOICE_STATUS_AUTHORISED = "AUTHORISED";
	
	// Invoice Line amount types
	// this means the provided unitAmount for each line item
	//  is the price before any tax is applied.
	static final String INVOICE_LINE_AMOUNT_TYPE_EXCLUSIVE = "EXCLUSIVE";
	static final String INVOICE_LINE_AMOUNT_TYPE_INCLUSIVE = "INCLUSIVE";
	static final String INVOICE_LINE_AMOUNT_TYPE_NOTAX = "NO_TAX";
	
	// Invoice tax calculation types
	static final String INVOICE_TAX_CALCULATION_TYPE_TAXCALC_AUTO = "TAXCALC/AUTO";

}
