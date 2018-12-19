package com.myproject.springboot.jpa.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.myproject.springboot.jpa.entity.StudentEntity;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is to test the Repository operations
 *
 * Things to note:
 *
 * * AbstractTestNGSpringContextTests - to hook TestNG and Spring together
 * * @Test - to trigger TestNG
 * * TestEntityManager - testing utility to help interact with the test data (like setting up certain states)
 */
@Test
@DataJpaTest
@Slf4j
public class StudentRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private StudentRepository studentRepository;

    public void dataProvided_retrieveStudent_studentFound() {
        log.info("Fetching student");

        Optional<StudentEntity> studentEntityOptional = studentRepository.findById(1L);

        // ensure there is a value
        assertThat(studentEntityOptional.isPresent()).isTrue();
        StudentEntity studentEntity = studentEntityOptional.get();

        // Check the data ==> 'John','Doe','JD456','2001-06-23');
        assertThat(studentEntity.getFirstName()).isEqualTo("John");
        assertThat(studentEntity.getLastName()).isEqualTo("Doe");
        assertThat(studentEntity.getBirthDate()).isEqualTo("2001-06-23");

    }
}
