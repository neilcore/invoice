package core.hubby.backend.tax.controller.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

public record TaxDetailResponse(
		@NotEmpty(message = "taxType component cannot be empty.")
		Set<String> taxType,
		@NotEmpty(message = "salaryTaxPeriod component cannot be empty.")
		Set<String> salesTaxPeriod
) {

}
