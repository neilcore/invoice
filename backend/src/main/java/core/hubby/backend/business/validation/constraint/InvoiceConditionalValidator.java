package core.hubby.backend.business.validation.constraint;

import core.hubby.backend.business.controller.dto.CreateInvoiceRequest;
import core.hubby.backend.business.validation.annotation.ValidateInvoice;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InvoiceConditionalValidator implements ConstraintValidator<ValidateInvoice, CreateInvoiceRequest> {

	@Override
	public boolean isValid(CreateInvoiceRequest value, ConstraintValidatorContext context) {
		if (value == null) return true; // let @NotNull handle the null DTO itself
		if (!value.contact().containsKey("contactId")) return false;
		
		/**
		 * If taxEligible is set to false, no tax type must be set.
		 * return false if tax type is found while taxEligible is
		 * set to false.
		 */
		if (!value.taxEligible()) {
			boolean presentTaxType = value.lineItems()
					.stream().anyMatch(el -> el.taxType() != null);
			if (presentTaxType) {
				return false;
			}
		}
		return true;
	}

}
