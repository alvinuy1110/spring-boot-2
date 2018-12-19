package com.myproject.springboot.cache.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Student implements Serializable {

    private long id;
    private String firstName;
    private String lastName;
    private String studentNumber;
    private Date birthDate;
}
