package com.myproject.springboot.validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.myproject.springboot.validation.domain.Employee;
import com.myproject.springboot.validation.domain.Employer;
import com.myproject.springboot.validation.domain.Sex;
import com.myproject.springboot.validation.domain.Student;
import com.myproject.springboot.validation.domain.Subject;
import com.myproject.springboot.validation.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

import static com.myproject.springboot.validation.util.TestDataUtil.createValidStudent;
import static org.assertj.core.api.Assertions.assertThat;

/*
Covers custom validation
 */
@Slf4j
public class CustomValidationTest {

    private Validator validator;

    @BeforeTest
    private void setup() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void invalidNameEmployee_validate_fail() {

        //given
        Employer employer = Employer.builder().build();

        Employee employee = createEmployee();
        employee.setFirstName("1111###");
     
        employer.setEmployee(employee);
        //when
        Set<ConstraintViolation<Employer>> violations = validator.validate(employer);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }


    @Test
    public void invalidNameInListEmployee_validate_fail() {

        //given
        Employer employer = Employer.builder().build();

        Employee employee = createEmployee();
        employee.setFirstName("1111###");
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        employer.setEmployees(employees);

        // valid in field
        employer.setEmployee(createEmployee());
        
        //when
        Set<ConstraintViolation<Employer>> violations = validator.validate(employer);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void invalidFirstNameEmployer_validate_fail() {

        //given
        Employer employer = Employer.builder().build();
        // valid in field
        employer.setEmployee(createEmployee());

        employer.setFirstName("ab");
        //when
        Set<ConstraintViolation<Employer>> violations = validator.validate(employer);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
        
        ConstraintViolation<Employer> constraintViolation = violations.iterator().next();
        assertThat(constraintViolation.getMessage()).isEqualTo("The employer firstName 'ab' must be between 5 and 14 characters long");
    }


    @Test
    public void validEmployee_validate_success() {

        //given
        Employer employer = Employer.builder().build();

        Employee employee = createEmployee();
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        employer.setEmployees(employees);
        employer.setEmployee(employee);

        //when
        Set<ConstraintViolation<Employer>> violations = validator.validate(employer);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isTrue();
    }
    
    private Employee createEmployee() {
        //@formatter:off
        Employee employee = Employee.builder()
                                    .id(1)
                                    .firstName("Jane")
                                    .lastName("Doe")
                .build();
                
        //@formatter:on
        return employee;
    }
}
