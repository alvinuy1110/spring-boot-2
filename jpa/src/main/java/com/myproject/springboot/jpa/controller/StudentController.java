package com.myproject.springboot.jpa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myproject.springboot.jpa.domain.Student;
import com.myproject.springboot.jpa.service.ResourceNotFoundException;
import com.myproject.springboot.jpa.service.StudentPagingService;
import com.myproject.springboot.jpa.service.StudentService;
import lombok.extern.slf4j.Slf4j;

/**
 * This is a CRUD controller
 */
@Controller
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentPagingService studentPagingService;

    /*
  This will fetch a page of students
   */
    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public ResponseEntity<Page<Student>> getStudent(Pageable pageable) {

        Page<Student> students = studentPagingService.getStudents(pageable);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /*
    This will fetch a single student based on the id
     */
    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
    public ResponseEntity<Student> getStudent(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {

        Student student = studentService.getStudent(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student newStudent = studentService.createStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/students/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long id,
            @Valid @RequestBody Student student) throws ResourceNotFoundException {
        Student newStudent = studentService.updateStudent(id, student);
        return new ResponseEntity<>(newStudent, HttpStatus.OK);
    }

    @RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStudent(@PathVariable(value = "id") Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
