package core.hubby.backend.tax.entities.embedded;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaxComponent {
	// Name of tax component
	@NotBlank(message = "name attribute cannot be blank.")
	@Column(name = "name", nullable = false)
	private String name;
	
	@NotNull(message = "rate attribute cannot be null.")
	@Column(nullable = false, scale = 4, precision = 7)
	private BigDecimal rate;
	
	@NotNull(message = "isCompound attribute cannot be null.")
	@Column(name = "is_compound")
	private Boolean isCompound;
	
	@Column(name = "non_recoverable")
	private Boolean nonRecoverable;
}
