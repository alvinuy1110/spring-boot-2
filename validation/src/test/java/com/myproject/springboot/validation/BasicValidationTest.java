package com.myproject.springboot.validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.myproject.springboot.validation.domain.Subject;
import com.myproject.springboot.validation.domain.Sex;
import com.myproject.springboot.validation.domain.Student;
import com.myproject.springboot.validation.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

import static com.myproject.springboot.validation.util.TestDataUtil.createValidStudent;
import static org.assertj.core.api.Assertions.assertThat;

/*
Covers basic validation
 */
@Slf4j
public class BasicValidationTest {

    private Validator validator;

    @BeforeTest
    private void setup() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();


//        ResourceBundleMessageInterpolator.
        Locale.setDefault(Locale.FRANCE);

    }

    @Test
    public void emptyStudent_validate_fail() {

        //given
        Student student = Student.builder().build();

        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
    }

    @Test
    public void validStudent_validate_success() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -10);
        //given
        Student student = createValidStudent();

        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void firstNameMaxLength_validate_fail() {

        //given
        Student student = createValidStudent();
        student.setFirstName("123456789012345678901");
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void lastNameEmpty_validate_fail() {

        //given
        Student student = createValidStudent();
        student.setLastName(" ");
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void birthDateInFuture_validate_fail() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        //given
        Student student = createValidStudent();
        student.setBirthDate(calendar.getTime());
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void nullSex_validate_fail() {
        //given
        Student student = createValidStudent();
        student.setSex(null);
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }


    @Test
    public void negativeOrZeroNumberOfSubjects_validate_fail() {
        //given
        Student student = createValidStudent();
        student.setNumberOfSubjects(0);
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void invalidEmailFormat_validate_fail() {
        //given
        Student student = createValidStudent();
        student.setEmail("blah");
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }


    @Test
    public void invalidClass_validate_fail() {
        List<String> classes = new ArrayList<>();
        classes.add(" ");
        //given
        Student student = createValidStudent();
        student.setClasses(classes);
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(1);
    }


    @Test
    public void invalidSubject_validate_fail() {
        Subject subject = new Subject();
        subject.setName(null);
        subject.setGrade(-2.05);

        List<Subject> subjects = new ArrayList<>();
        subjects.add(subject);

        //given
        Student student = createValidStudent();
        student.setSubjects(subjects);
        //when
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        log.info("==========================");
        ValidationUtil.debug(violations);
        //then
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.size()).isEqualTo(2);
    }
  
}
