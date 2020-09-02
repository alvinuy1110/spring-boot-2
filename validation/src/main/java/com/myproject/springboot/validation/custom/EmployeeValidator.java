package com.myproject.springboot.validation.custom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.myproject.springboot.validation.domain.Employee;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom employee validator.  The syntax is ConstraintValidator<annotation_name, object_type>
 */
@Slf4j
public class EmployeeValidator implements ConstraintValidator<ValidateEmployee, Employee> {

    @Override
    public void initialize(ValidateEmployee constraintAnnotation) {

        log.info("Inside initialize, Message:{}, Payload:{}", constraintAnnotation.message(), constraintAnnotation
                .payload());

    }

    @Override
    public boolean isValid(Employee value, ConstraintValidatorContext constraintValidatorContext) {
        // TODO implement custom logic here
        
        // valid chars
        String regex ="^[a-zA-Z]*$";
        if (value !=null) {
            String firstName = value.getFirstName();
            if (firstName!=null) {
                if (firstName.matches(regex)) {
                    return  true;
                }
            }
        }
        
        return false;
    }
}
