package core.hubby.backend.business.entities.embedded;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DefaultCurrency implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotNull(message = "name cannot be null.")
	private String name;
	@NotNull(message = "symbol cannot be null.")
	private String symbol;
	@NotNull(message = "currencyCode cannot be null.")
	private String currencyCode;
}
