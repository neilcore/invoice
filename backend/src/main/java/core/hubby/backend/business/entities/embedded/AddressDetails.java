package core.hubby.backend.business.entities.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class AddressDetails {
	
	@Column(name = "physical_address_street", nullable = false)
	@NotBlank(message = "Physical address street is mandatory")
	private String physicalAddressStreet;
	
	@Column(name = "physical_address_city", nullable = false)
	@NotBlank(message = "Physical address city is mandatory")
	private String physicalAddressCity;
	
	@Column(name = "physical_address_state", nullable = false)
	@NotBlank(message = "Physical Address State cannot be blank")
	private String physicalAddressState; // state/province/region
	
	@Column(name = "physical_address_postal_code", nullable = false)
	@NotBlank(message = "Physical address postal code is mandatory")
	private String physicalAddressPostalCode;
	
	@Column(name = "postal_address_street")
    private String postalAddressStreet; // Optional: If different from physical
	
	@Column(name = "postal_address_city")
    private String postalAddressCity;
	
	@Column(name = "postal_address_state")
    private String postalAddressState;
	
	@Column(name = "postal_address_postal_code")
    private String postalAddressPostalCode;
}
