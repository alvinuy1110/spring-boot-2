package com.myproject.springboot.rabbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myproject.springboot.rabbit.domain.MessageEvent;
import com.myproject.springboot.rabbit.service.MessageService;

@Controller
public class RabbitController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public ResponseEntity<?> publishDemo() {

        MessageEvent messageEvent = new MessageEvent("name1","desc1");
        messageService.publish(messageEvent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @RequestMapping(value = "/students", method = RequestMethod.POST)
    //    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
    //        Student newStudent = studentService.createStudent(student);
    //        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    //    }
}
