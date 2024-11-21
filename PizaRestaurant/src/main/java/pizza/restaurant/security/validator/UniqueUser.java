package pizza.restaurant.security.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {UniqueUserValidator.class})
public @interface UniqueUser {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
