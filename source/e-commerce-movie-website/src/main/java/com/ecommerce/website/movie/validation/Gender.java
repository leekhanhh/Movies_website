package com.ecommerce.website.movie.validation;

import com.ecommerce.website.movie.validation.impl.GenderValidation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderValidation.class)
public @interface Gender {
    boolean allowNull() default false;

    String message() default "Invalid gender";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
