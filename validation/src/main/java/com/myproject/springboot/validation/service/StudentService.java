package com.myproject.springboot.validation.service;

import com.myproject.springboot.validation.domain.Student;

public interface StudentService {

    Student getStudent();

    Student validateStudent(Student student);
}
