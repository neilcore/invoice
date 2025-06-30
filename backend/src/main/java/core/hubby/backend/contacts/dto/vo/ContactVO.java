package core.hubby.backend.contacts.dto.vo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ContactVO (
		UUID contactId,
		String contactNumber,
		String accountNumber,
		String contactStatus,
		String name,
		String firstName,
		String lastName,
		String emailAddress,
		String companyNumber,
		String taxNumber,
		List<Map<String, String>> addresses,
		List<Map<String, String>> phone,
		Boolean isSupplier,
		Boolean isCustomer
) {
	public ContactVO {
		if (contactId == null) {
			throw new IllegalArgumentException("Contact ID cannot be null");
		}
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be blank");
		}
	}
}
