package core.hubby.backend.tax.controller.dto;

import java.util.UUID;

import core.hubby.backend.tax.entities.embedded.TaxNumber;
import core.hubby.backend.tax.validation.annotation.ValidateTaxDetailsCondition;
import jakarta.validation.constraints.NotNull;

@ValidateTaxDetailsCondition // My custom validation
public record CreateTaxDetailsRequests(
		@NotNull(message = "organizationID component cannot be null.")
		UUID organizationID,
		@NotNull(message = "paysTax component cannot be null.")
		Boolean paysTax,
		TaxNumber taxNumber ,
		UUID salesTaxBasis,
		String taxPeriod,
		String defaultSalesTax,
		String defaultTaxPurchases
) {
}
