package com.myproject.springboot.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myproject.springboot.cache.domain.Student;
import com.myproject.springboot.cache.service.StudentService;

/**
 * Created by user on 19/12/18.
 */
@Controller
public class CacheController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
    public ResponseEntity<Student> getStudent(@PathVariable(value = "id") Long id) {
        Student student =null;
        if (id ==2) {
            student = studentService.getStudent2(id);
        } else {
            student = studentService.getStudent(id);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

}
