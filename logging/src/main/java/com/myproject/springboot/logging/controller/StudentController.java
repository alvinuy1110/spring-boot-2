package com.myproject.springboot.logging.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.myproject.springboot.logging.aspect.annotation.MDCMarker;
import com.myproject.springboot.logging.aspect.annotation.MDCValue;
import com.myproject.springboot.logging.constants.MDCConstants;
import com.myproject.springboot.logging.domain.Student;
import com.myproject.springboot.logging.service.StudentService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    @MDCMarker({@MDCValue(value = MDCConstants.FNAME, content = "John"),
            @MDCValue(value =MDCConstants.LNAME, content = "Doe")})
    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public ResponseEntity<Student> getStudent(@RequestParam(value = "id", required = false) long id) {

        Student student = new Student();
        return new ResponseEntity<>(studentService.getStudent(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public ResponseEntity<Student> postProperties(@RequestBody @Valid Student student) {

        return new ResponseEntity<>(studentService.getStudent(), HttpStatus.CREATED);
    }

}
