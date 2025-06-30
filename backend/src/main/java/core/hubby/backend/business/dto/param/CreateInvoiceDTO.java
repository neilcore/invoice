package core.hubby.backend.business.dto.param;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record CreateInvoiceDTO(
		UUID invoiceType,
		String contact,
		List<Map<String, Object>> lineItems,
		String lineAmountType,
		String date,
		String dueDate,
		String status,
		String reference
) {
	public CreateInvoiceDTO {
		Objects.requireNonNull(invoiceType, "Invoice type cannot be null");
		Objects.requireNonNull(contact, "Contact cannot be null");
		Objects.requireNonNull(lineItems, "Line items cannot be null");
	}
}
