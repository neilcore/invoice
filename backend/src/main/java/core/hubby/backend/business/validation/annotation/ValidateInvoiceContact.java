package core.hubby.backend.business.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import core.hubby.backend.business.validation.constraint.InvoiceContactConditionalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InvoiceContactConditionalValidator.class)
@Documented
public @interface ValidateInvoiceContact {
    String message() default "Invalid contact component value. Missing key named \"contactId\" ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
