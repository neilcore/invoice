package core.hubby.backend.business.entities.embedded;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LineItemSettings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double defaultQuantity;
	private String defaultAccountCode;
	private Integer defaultDiscountRate;
}
