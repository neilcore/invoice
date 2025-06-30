package core.hubby.backend.business.entities.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class TaxDetails {
	private String taxIdNo; // e.g., GST, VAT, ABN, etc.
	private String taxBasis;
	private String taxPeriod;
}
