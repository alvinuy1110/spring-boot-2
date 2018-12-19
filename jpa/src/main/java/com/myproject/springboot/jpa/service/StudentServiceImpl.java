package com.myproject.springboot.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.myproject.springboot.jpa.domain.Student;
import com.myproject.springboot.jpa.entity.StudentEntity;
import com.myproject.springboot.jpa.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the business logic.  It bridges the Domain and Entity objects
 */

@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student getStudent(long id) throws ResourceNotFoundException {
        log.debug("Retrieving student with id {}", id);
        StudentEntity studentEntity = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id: " + id)

        );

        return convert(studentEntity);

    }

    @Override
    public Student createStudent(Student student) {
        StudentEntity studentEntity = convert(student);
        StudentEntity newStudentEntity = studentRepository.save(studentEntity);
        return convert(newStudentEntity);
    }

    @Override
    public Student updateStudent(long id, Student student) throws ResourceNotFoundException {
        StudentEntity studentEntity = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id: " + id)

        );

        StudentEntity inputStudentEntity = convert(student);
        inputStudentEntity.setId(studentEntity.getId());

        StudentEntity newStudentEntity = studentRepository.save(inputStudentEntity);
        return convert(newStudentEntity);
    }


    @Override
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    private Student convert(StudentEntity studentEntity) {
        if (studentEntity == null) {
            return null;
        }

        Student student = new Student();
        student.setId(studentEntity.getId());
        student.setFirstName(studentEntity.getFirstName());
        student.setLastName(studentEntity.getLastName());
        student.setStudentNumber(studentEntity.getStudentNumber());
        student.setBirthDate(studentEntity.getBirthDate());

        return student;
    }

    private StudentEntity convert(Student student) {
        if (student == null) {
            return null;
        }

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(student.getId());
        studentEntity.setFirstName(student.getFirstName());
        studentEntity.setLastName(student.getLastName());
        studentEntity.setStudentNumber(student.getStudentNumber());
        studentEntity.setBirthDate(student.getBirthDate());

        return studentEntity;
    }
}
