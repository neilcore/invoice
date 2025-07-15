package core.hubby.backend.tax.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import core.hubby.backend.tax.validation.constraint.TaxDetailsConditionalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TaxDetailsConditionalValidator.class)
@Documented
public @interface ValidateTaxDetailsCondition {
    String message() default "Tax details (taxNumber, salesTaxBasis, taxPeriod, defaultSalesTax) must be null if paysTax is false, or at least one must be present if paysTax is true.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
