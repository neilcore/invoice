package core.hubby.backend.tax.entities.embedded;

import java.io.Serializable;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TaxTypes implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "type atrribute cannot be blank.")
	private String type;
	
	@NotNull(message = "rate attribute cannot be null.")
	@Digits(integer = 4, fraction = 2)
	private Double rate;
	
	@NotBlank(message = "name attribute cannot be blank.")
	private String name;
	
	// VAT or GST
	private String component;
	
	@NotNull(message = "systemDefined attribute cannot be null.")
	private Boolean systemDefined;

}
