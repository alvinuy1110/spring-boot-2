package com.myproject.springboot.cache.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.myproject.springboot.cache.domain.Student;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the business logic.  We will put the caching functionality here.
 */

@Slf4j
public class StudentServiceImpl implements StudentService {

    @Override
    @Cacheable(value = "cache1")
    public Student getStudent(long id) {
        log.info("Retrieving student with id {}", id);

        Student student = new Student();
        student.setId(id);
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setStudentNumber("456JJJ");

        return student;

    }

    @Override
    @Cacheable(value = "cache2")
    public Student getStudent2(long id) {
        log.info("Retrieving student2 with id {}", id);

        Student student = new Student();
        student.setId(id);
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setStudentNumber("456JJJ");

        return student;

    }
    @CacheEvict(value = "cache1")
    public void evictStudent1() {

    }
}
