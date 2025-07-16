package core.hubby.backend.business.controller.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record InvoiceResponse (List<Map<String, String>> invoices) {
	// Nested record class addresses
	record Contact (
			UUID contactId, String contactStatus,
			String name, List<Map<String, String>> address,
			List<Map<String, String>> phones, String updatedDateUtc,
			String isSupplier, String isCustomer
	) {
		public Contact {
			Objects.requireNonNull(contactId, "contactId cannot be null");
			Objects.requireNonNull(contactStatus, "contactStatus cannot be null");
			Objects.requireNonNull(name, "name cannot be null");
			Objects.requireNonNull(address, "address cannot be null");
			Objects.requireNonNull(phones, "phones cannot be null");
			Objects.requireNonNull(updatedDateUtc, "updatedDateUtc cannot be null");
			Objects.requireNonNull(isSupplier, "isSupplier cannot be null");
			Objects.requireNonNull(isCustomer, "isCustomer cannot be null");
		}
	}
	
	// Nested record class LineItems
	record LineItems(List<Map<String, Object>> lineItems) {
		public LineItems {
			Objects.requireNonNull(lineItems, "lineItems cannot be null");
		}
	}
	
	// Nested record class Payments
	record Payments(List<Map<String, String>> payments) {
	}
}
