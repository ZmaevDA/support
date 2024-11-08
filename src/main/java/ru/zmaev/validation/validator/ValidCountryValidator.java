package ru.zmaev.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.zmaev.domain.enums.Country;
import ru.zmaev.validation.annotation.ValidCountry;

import java.util.Arrays;

public class ValidCountryValidator implements ConstraintValidator<ValidCountry, String> {
    @Override
    public boolean isValid(String country, ConstraintValidatorContext context) {
        return Arrays.stream(Country.values()).anyMatch(c -> c.name().equalsIgnoreCase(country));
    }
}
