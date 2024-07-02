package com.ecommerce.website.movie.validation.impl;


import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.validation.Gender;

import javax.validation.ConstraintValidator;
import java.util.Objects;

public class GenderValidation implements ConstraintValidator<Gender,Integer> {
    private boolean allowNull;
    @Override
    public void initialize(Gender constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }
    @Override
    public boolean isValid(Integer value, javax.validation.ConstraintValidatorContext context) {
        if (value == null && allowNull) {
            return true;
        }
        return Objects.equals(value, Constant.MALE) || Objects.equals(value, Constant.FEMALE) ;
    }
}
