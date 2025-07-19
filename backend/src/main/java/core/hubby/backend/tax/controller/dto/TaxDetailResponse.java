package core.hubby.backend.tax.controller.dto;

import java.util.Set;

import core.hubby.backend.tax.entities.embedded.TaxTypes;
import jakarta.validation.constraints.NotEmpty;

public record TaxDetailResponse(
		@NotEmpty(message = "taxType component cannot be empty.")
		Set<TaxTypes> taxType,
		@NotEmpty(message = "salaryTaxPeriod component cannot be empty.")
		Set<String> salesTaxPeriod
) {

}
