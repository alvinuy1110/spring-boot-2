package com.myproject.springboot.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myproject.springboot.jpa.domain.Student;

public interface StudentPagingService extends  StudentService{

    Page<Student> getStudents(Pageable pageable);
}
