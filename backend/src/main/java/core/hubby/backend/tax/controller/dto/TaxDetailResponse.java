package core.hubby.backend.tax.controller.dto;

import java.util.Set;

import core.hubby.backend.tax.entities.embedded.TaxTypes;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TaxDetailResponse(
		AvailableTaxTypes taxTypes,
		@NotEmpty(message = "salaryTaxPeriod component cannot be empty.")
		Set<String> salesTaxPeriod
) {
	public record AvailableTaxTypes(
			@NotNull(message = "available component cannot be null")
			Boolean available,
			@NotEmpty(message = "taxType component cannot be empty.")
			Set<TaxTypes> taxType
	) {}
}
