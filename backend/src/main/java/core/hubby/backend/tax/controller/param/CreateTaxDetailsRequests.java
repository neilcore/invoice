package core.hubby.backend.tax.controller.param;

import jakarta.validation.constraints.NotNull;

public record CreateTaxDetailsRequests(
		boolean paysTax,
		TaxNumber taxNumber 
) {
	public record TaxNumber(
			@NotNull(message = "label component is required") String label,
			@NotNull(message = "number component is required")
			String number
	) {}
}
