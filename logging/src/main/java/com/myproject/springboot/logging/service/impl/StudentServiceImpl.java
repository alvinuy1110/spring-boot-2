package com.myproject.springboot.logging.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.logging.MDC;

import com.myproject.springboot.logging.aspect.annotation.MDCMarker;
import com.myproject.springboot.logging.aspect.annotation.MDCValue;
import com.myproject.springboot.logging.domain.Student;
import com.myproject.springboot.logging.service.StudentService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StudentServiceImpl implements StudentService {

    @MDCMarker
    @Override
    public Student getStudent() {


        log.info("logging once");

        log.info("logging again");
        return createValidStudent();
    }

    @MDCMarker
    @Override
    public Student getStudent(@MDCValue("custom.id") long id) {


        log.info("logging once");

        log.info("logging again");
        return createValidStudent();
    }
    @Override
    public Student validateStudent(Student student) {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        //        Set<ConstraintViolation<Student>> violations =validator.validate(student);
        return null;
    }

    public static Student createValidStudent() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -10);

        //@formatter:off
        Student student = Student.builder()
                                 .id(1)
                                 .firstName("john")
                                 .lastName("doe")
                                 .birthDate(calendar.getTime())
                                 .numberOfSubjects(3)
                                 .email("john_doe@example.com")
                                 
                                 
                                 .classes(new ArrayList<>())
                                 .build();

        //@formatter:on
        return student;
    }

}
