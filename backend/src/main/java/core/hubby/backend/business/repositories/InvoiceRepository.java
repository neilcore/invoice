package core.hubby.backend.business.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import core.hubby.backend.business.entities.Invoice;
import jakarta.transaction.Transactional;

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
	
	// Invoice type
	static final String INVOICE_TYPE_CUSTOMER_INVOICE = "CUSTOMER_INVOICE";
	static final String INVOICE_TYPE_SUPPLIER_INVOICE = "SUPPLIER_INVOICE";
	
	@Modifying
	@Transactional
	@Query("UPDATE Invoice i SET i.status = :status WHERE i.invoiceId = :invoiceId")
	int updateInvoiceStatus(
			@Param("invoiceId") UUID invoiceId,
			@Param("status") String status
	);

}
