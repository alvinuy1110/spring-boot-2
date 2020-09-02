package com.myproject.springboot.logging.service;

import org.jboss.logging.MDC;

import com.myproject.springboot.logging.aspect.annotation.MDCMarker;
import com.myproject.springboot.logging.domain.Student;

public interface StudentService {

    Student getStudent();

    Student getStudent(long id);
    Student validateStudent(Student student);
}
