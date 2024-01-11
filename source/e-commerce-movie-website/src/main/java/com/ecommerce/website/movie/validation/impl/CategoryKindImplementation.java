package com.ecommerce.website.movie.validation.impl;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.validation.CategoryKind;

import javax.validation.ConstraintValidator;
import java.util.Objects;

public class CategoryKindImplementation implements ConstraintValidator<CategoryKind,Integer> {

    private boolean allowNull;

    @Override
    public void initialize(CategoryKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer categoryKind, javax.validation.ConstraintValidatorContext constraintValidatorContext) {
        if (categoryKind == null && allowNull) {
            return true;
        }
        return Objects.equals(categoryKind, Constant.CATEGORY_KIND_MOVIE_GENRE) ||
                Objects.equals(categoryKind, Constant.CATEGORY_KIND_MOVIE_SERVICE);
    }
}
