package core.hubby.backend.business.dto.param;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import core.hubby.backend.business.validation.annotation.ValidateInvoiceContact;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateInvoiceRequest(
		@NotNull(message = "invoiceType component cannot be null.")
		String invoiceType,
		@NotNull(message = "contact component cannot be null")
		@ValidateInvoiceContact
		Map<String, Object> contact,
		@NotNull(message = "lineAmounType component cannot be null")
		@Pattern(regexp = "^[A-Z](?:[A-Z]|_[A-Z])*$", message = "Invalid value for lineAmountType component.")
		String lineAmountType,
		@NotNull(message = "lineItems component cannot be null.")
		Set<LineItems> lineItems,
		@NotNull(message = "date component cannot be null.")
		LocalDate date,
		@Future(message = "Invalid value for dueDate component.")
		@DateTimeFormat(iso = ISO.DATE)
		LocalDate dueDate,
		String status,
		String reference
) {
	public record LineItems(
			@NotNull(message = "description component cannot be null.")
			String description,
			@NotNull(message = "quantity component cannot be null.")
			@DecimalMin("1.0")
			@Digits(fraction = 1, integer = 3)
			Double quantity,
			@NotNull(message = "unitAmount component cannot be null.")
			@Digits(fraction = 2, integer = 6)
			Double unitAmount,
			String accountCode,
			@Pattern(regexp = "^[A-Z](?:[A-Z]|_[A-Z])*$", message = "Invalid value for taxType component.")
			UUID taxType,
			Integer discountRate
	) {
	}
}
