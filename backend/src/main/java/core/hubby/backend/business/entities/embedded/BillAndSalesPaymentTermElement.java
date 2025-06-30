package core.hubby.backend.business.entities.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillAndSalesPaymentTermElement {
	@Min(0)
	@Max(31)
	@PositiveOrZero(message = "Invalid day component value")
	private Integer day;
	
	@Size(max = 100)
	private String paymentTerm;
}
