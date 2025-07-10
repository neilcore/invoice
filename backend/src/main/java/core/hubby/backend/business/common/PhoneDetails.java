package core.hubby.backend.business.common;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO - read about hibernate's SerializableJavaType 
// https://www.baeldung.com/jpa-entities-serializable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Set<String> phoneTypes = Set.of(
			"DEFAULT", "DDI", "MOBILE", "FAX"
	);
	
	@NotNull(message = "phoneType attribute cannot be null.")
	private String phoneType;
	@NotNull(message = "phoneNumber attribute cannot be null.")
	private String phoneNumber;
	private String phoneAreaCode;
	private String phoneCountryCode;
	private Boolean isDefault;
	
	public void setPhoneType(String phoneType) {
		if(!phoneTypes.contains(phoneType)) {
			throw new IllegalArgumentException("Invalid key found: '" + phoneType + "'. Allowed keys are: " + phoneTypes);
		} else {
			this.phoneType = phoneType;
		}
	}
	
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault ? Boolean.TRUE : Boolean.FALSE;
	}
}
