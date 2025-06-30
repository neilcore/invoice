package core.hubby.backend.business.entities.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class FinancialSettings {
	@NotBlank(message = "Default currency is mandatory")
	private String defaultCurrency;
	@NotBlank(message = "Default tax basis is mandatory")
	private String timeZone;

}
