package core.hubby.backend.core.dto;

public record PhoneDetail (
		String phoneType,
		String phoneNumber,
		String phoneAreaCode,
		String phoneCountryCode,
		Boolean isDefault
) {
	public PhoneDetail {
		if (phoneType.isBlank() || phoneType.isEmpty()) {
			throw new IllegalArgumentException("phoneType component cannot be null");
		}
		if (phoneNumber.isBlank() || phoneNumber.isEmpty()) {
			throw new IllegalArgumentException("phoneNumber component cannot be null.");
		}
		if (phoneAreaCode.isBlank() || phoneAreaCode.isEmpty()) {
			throw new IllegalArgumentException("phoneAreaCode component cannot be null.");
		}
	}
}
