package core.hubby.backend.tax.controller.dto;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaxRateRequests(
		@NotBlank(message = "name component cannot be blank.")
		String name,
		@NotBlank(message = "taxType component cannot be null.")
		String taxType,
		List<Component> taxComponents,
		@NotNull(message = "effectiveRate component cannot be null.")
		BigDecimal effectiveRate,
		ApplyToAccounts applyToAccounts,
		Boolean systemDefined,
		@NotBlank(message = "status component cannot be blank.")
		String status
) {
	public record Component(
			@NotBlank(message = "name component cannot be blank.")
			String name,
			@NotNull(message = "rate component cannot be null.")
			BigDecimal rate,
			Boolean isCompound,
			Boolean nonRecoverable
	) {}
	
	public record ApplyToAccounts(
			Boolean applyToAssetAccount,
			Boolean applyToEquityAccount,
			Boolean applyToExpensesAccount,
			Boolean applyToLiabilitiesAccount,
			Boolean applyToRevenueAccount
	) {}
}
