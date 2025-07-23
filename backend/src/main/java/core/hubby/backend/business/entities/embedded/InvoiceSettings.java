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
public class InvoiceSettings implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String defaultLineAmountType;
	private String status;
	
	public void setStatus(String stat) {
		// DRAFT is the default status
		this.status = stat.isBlank() ? "DRAFT" : stat;
	}
}
