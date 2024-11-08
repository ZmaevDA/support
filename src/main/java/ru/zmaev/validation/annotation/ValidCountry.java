package ru.zmaev.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.zmaev.validation.validator.ValidCountryValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCountryValidator.class)
public @interface ValidCountry {
    String message() default "Country must be a valid country";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
