package core.hubby.backend.core.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CountriesApiDTO(
		@NotNull(message = "name component cannot be null.")
		CountryName name,
		@Size(min = 2, max = 2)
		@NotBlank(message = "cca2 component cannot be null")
		String cca2
) {
	public record CountryName(
			@NotBlank(message = "common component cannot be blank")
			String common,
			@NotBlank(message = "official component cannot be null")
			String official,
			NativeNames nativeName
	) {
		public record NativeNames(Map<String, String> eng, Map<String, String> fil) {}
	}
}
