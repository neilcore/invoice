package core.hubby.backend.business.controller.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InvoiceResponse (
		String lineAmountType
) {
	public record LineItems(
			@NotBlank(message = "description component cannot be blank.")
			String description,
			@NotBlank(message = "taxType component cannot be blank.")
			String taxType,
			double quantity,
			double unitAmount,
			@NotNull(message = "totalForCustomer component cannot be null.")
			BigDecimal totalForCustomer
	) {}
}
