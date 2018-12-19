package com.myproject.springboot.jpa.service;

import com.myproject.springboot.jpa.domain.Student;

public interface StudentService {

    Student getStudent(long id) throws ResourceNotFoundException;

    Student createStudent(Student student);

    Student updateStudent(long id, Student student) throws ResourceNotFoundException;

    void deleteStudent(long id);
}
