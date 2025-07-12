package core.hubby.backend.core.dto;
import java.util.Map;
import jakarta.validation.constraints.NotNull;

public record CountriesDTO(
		@NotNull(message = "name component is not null.")
		NameDetails name,
		@NotNull(message = "cca2 component cannot be null.")
		String cca2,
		@NotNull(message = "currencies component cannot be null.")
		Map<String, Map<String, String>> currencies
		
) {
	public record NameDetails(
			@NotNull(message = "common component is not null.")
			String common,
			@NotNull(message = "official component is not null.")
			String official,
			Map<String, Map<String, String>> nativeName
	) {
	}
}
