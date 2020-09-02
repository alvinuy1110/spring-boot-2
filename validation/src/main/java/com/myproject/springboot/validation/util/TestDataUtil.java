package com.myproject.springboot.validation.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.myproject.springboot.validation.domain.Sex;
import com.myproject.springboot.validation.domain.Student;
import com.myproject.springboot.validation.domain.Subject;

public class TestDataUtil {

    public static Student createValidStudent() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -10);

        List<Subject> subjects = new ArrayList<>();
        subjects.add(createSubject() );
        //@formatter:off
        Student student = Student.builder()
                                 .id(1)
                                 .firstName("john")
                                 .lastName("doe")
                                 .birthDate(calendar.getTime())
                                 .numberOfSubjects(3)
                                 .sex(Sex.MALE)
                                 .email("john_doe@example.com")
                                 
                                 
                                 .classes(new ArrayList<>())
                                 .subjects(subjects)
                                 .build();

        //@formatter:on
        return student;
    }

    public static Subject createSubject() {
        Subject subject = new Subject();
        subject.setName("subject1");
        subject.setGrade(2.05);

        return subject;
    }
}
