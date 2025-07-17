package core.hubby.backend.tax.controller.dto;
import java.util.UUID;

import core.hubby.backend.tax.entities.SalesTaxBasis;
import core.hubby.backend.tax.entities.embedded.TaxNumber;

public record TaxResponse(
		UUID taxDetailsID,
		Organization organization,
		boolean paysTax,
		TaxNumber taxNumber,
		String defaultSalesTax,
		SalesTaxBasis salesTaxBasis,
		String taxPeriod,
		String defaultTaxPurchases
) {
	public record Organization(UUID organizationID) {}
}
