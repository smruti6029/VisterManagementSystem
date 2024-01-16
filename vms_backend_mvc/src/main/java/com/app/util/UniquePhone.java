package com.app.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.app.exception.UniquePhoneValidator;

@Documented
@Constraint(validatedBy = UniquePhoneValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhone {

    String message() default "PhoneNumber is already in use";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}