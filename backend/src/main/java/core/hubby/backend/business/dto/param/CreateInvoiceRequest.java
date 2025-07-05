package core.hubby.backend.business.dto.param;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateInvoiceRequest(
		@NotNull(message = "invoiceType component cannot be null.")
		UUID invoiceType,
		@NotNull(message = "contact component cannot be null")
		Map<String, Object> contact,
		Set<LineItems> lineItems,
		@NotNull(message = "lineAmounType component cannot be null")
		@Pattern(regexp = "^[A-Z](?:[A-Z]|_[A-Z])*$", message = "Invalid value for lineAmountType component.")
		String lineAmountType,
		LocalDate date,
		@FutureOrPresent(message = "Invalid value for dueDate component.")
		@DateTimeFormat(iso = ISO.DATE)
		LocalDate dueDate,
		String status,
		String reference
) {
	public CreateInvoiceRequest(
			UUID invoiceType,
			Map<String, Object> contact,
			Set<LineItems> lineItems,
			String lineAmountType,
			LocalDate date,
			LocalDate dueDate,
			String status,
			String reference
	) {
		this.invoiceType = invoiceType;
		
		if (!contact.containsKey("contactId")) {
			throw new IllegalArgumentException("Invalid value for contact component.");
		} else {
			this.contact = contact;
		}
		this.lineItems = lineItems;
		this.lineAmountType = lineAmountType;
		this.date = date;
		this.dueDate = dueDate;
		this.status = status.isBlank() || status.isEmpty() ? "DRAFT" : status;
		this.reference = reference;
	}
	
	public record LineItems(
			@NotNull(message = "description component cannot be null.")
			String description,
			@NotNull(message = "quantity component cannot be null.")
			Double quantity,
			@NotNull(message = "unitAmount component cannot be null.")
			Double unitAmount,
			String accountCode,
			@Pattern(regexp = "^[A-Z](?:[A-Z]|_[A-Z])*$", message = "Invalid value for taxType component.")
			UUID taxType
	) {}
}
