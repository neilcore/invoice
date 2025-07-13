package core.hubby.backend.business.entities.embedded;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PhoneDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "phoneType attribute cannot be null.")
	private String phoneType;
	
	@NotNull(message = "phoneNumber attribute cannot be null.")
	private String phoneNumber;
	
	private String phoneAreaCode;
	
	private String phoneCountryCode;
	
	private Boolean isDefault;
	
	public PhoneDetails(
			String phoneType,
			String phoneNumber,
			String phoneAreaCode,
			String phoneCountryCode,
			Boolean isDefault
	) {
		this.phoneType = phoneType;
		this.phoneNumber = phoneNumber;
		this.phoneAreaCode = phoneAreaCode;
		this.phoneCountryCode = phoneCountryCode;
		this.isDefault = isDefault;
	}
	
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault ? Boolean.TRUE : Boolean.FALSE;
	}
}
