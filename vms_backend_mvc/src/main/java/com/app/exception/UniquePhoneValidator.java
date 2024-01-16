package com.app.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.service.VisitorService;
import com.app.util.UniquePhone;

@Component
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    private final VisitorService visitorService;

    @Autowired
    public UniquePhoneValidator(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return phone != null && !visitorService.isPhoneAlreadyInUse(phone);
    }
}
