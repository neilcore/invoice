package core.hubby.backend.contacts.dto.param;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateContactParam (
		String name,
		String firstName,
		String lastName,
		String emailAddress,
		String contactNumber,
		String accountNumber,
		String companyNumber,
		String taxNumber,
		Boolean isSupplier,
		Boolean isCustomer,
		List<Map<String, Object>> address,
		List<Map<String, Object>> phone,
		UUID paymentTerms
) {
	public CreateContactParam(String name) {
		this(name, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	public CreateContactParam {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be blank");
		}
	}
}
