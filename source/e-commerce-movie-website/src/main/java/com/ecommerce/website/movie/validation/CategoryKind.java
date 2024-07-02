package com.ecommerce.website.movie.validation;

import com.ecommerce.website.movie.validation.impl.CategoryKindImplementation;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryKindImplementation.class)
@Documented
public @interface CategoryKind {
    boolean allowNull() default false;

    String message() default "Category kind invalid!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
