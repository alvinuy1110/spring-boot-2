package com.myproject.springboot.validation.service.impl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.myproject.springboot.validation.domain.Student;
import com.myproject.springboot.validation.service.StudentService;
import com.myproject.springboot.validation.util.TestDataUtil;

public class StudentServiceImpl implements StudentService {

    @Override
    public Student getStudent() {

        return TestDataUtil.createValidStudent();
    }

    @Override
    public Student validateStudent(Student student) {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

//        Set<ConstraintViolation<Student>> violations =validator.validate(student);
        return null;
    }
}
