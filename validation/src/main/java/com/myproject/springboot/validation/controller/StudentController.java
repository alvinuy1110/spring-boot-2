package com.myproject.springboot.validation.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myproject.springboot.validation.domain.Student;
import com.myproject.springboot.validation.service.StudentService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;
    
//    @RequestMapping(value = "/validateStudent", method = RequestMethod.GET)
//    public ResponseEntity<Student> getStudent() {
//
//        Student student = new Student();
//        return new ResponseEntity<>(studentService.validateStudent(student), HttpStatus.OK);
//    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public ResponseEntity<Student> postProperties(@Valid @RequestBody Student student) {

        return new ResponseEntity<>(studentService.getStudent(), HttpStatus.CREATED);
    }

}
