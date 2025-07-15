package core.hubby.backend.tax.validation.constraint;

import core.hubby.backend.tax.controller.param.CreateTaxDetailsRequests;
import core.hubby.backend.tax.validation.annotation.ValidateTaxDetailsCondition;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TaxDetailsConditionalValidator implements ConstraintValidator<ValidateTaxDetailsCondition, CreateTaxDetailsRequests> {
    
	@Override
	public boolean isValid(CreateTaxDetailsRequests value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null DTO itself
        }

        boolean paysTax = value.paysTax();
        boolean hasTaxDetails = value.taxNumber() != null || 
                                value.salesTaxBasis() != null || 
                                value.taxPeriod() != null || 
                                value.defaultSalesTax() != null;

        if (!paysTax && hasTaxDetails) {
            // If paysTax is false, none of the tax details should be present
            context.disableDefaultConstraintViolation(); // Disable default message
            context.buildConstraintViolationWithTemplate("Tax details (taxNumber, salesTaxBasis, taxPeriod, defaultSalesTax) must be null if paysTax is false.")
                   .addConstraintViolation();
            return false;
            
        } else if (paysTax && !hasTaxDetails) {
            // If paysTax is true, at least one tax detail should be present
            context.disableDefaultConstraintViolation(); // Disable default message
            context.buildConstraintViolationWithTemplate("At least one of taxNumber, salesTaxBasis, taxPeriod, or defaultSalesTax must be present if paysTax is true.")
                   .addConstraintViolation();
            return false;
        }

        return true; // Validation passes
	}

}
