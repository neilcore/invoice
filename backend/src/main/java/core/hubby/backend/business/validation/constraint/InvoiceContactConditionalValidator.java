package core.hubby.backend.business.validation.constraint;

import java.util.Map;
import core.hubby.backend.business.validation.annotation.ValidateInvoiceContact;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InvoiceContactConditionalValidator implements ConstraintValidator<ValidateInvoiceContact, Map<String, Object>> {

	@Override
	public boolean isValid(Map<String, Object> value, ConstraintValidatorContext context) {
		if (value == null) return true; // let @NotNull handle the null DTO itself
		if (!value.containsKey("contactId")) return false;
		return true;
	}

}
