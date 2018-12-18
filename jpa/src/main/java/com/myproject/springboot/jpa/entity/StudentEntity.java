package com.myproject.springboot.jpa.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * @Entity - indicates that this object will be mapped to an actual database object
 * @Table - table to associate with, provided for with "name"
 *
 * Things to note:
 *
 *
 */
@Data
@Entity
@Table(name = "tbl_student")
public class StudentEntity {

    /*
    The @Id indicates the primary key
    @Column which column in the table
    @GeneratedValue - indicates how the id is generated
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "student_number", nullable = false)
    private String studentNumber;

    /*
    The nullable is set to true, indicating that it is allowed to not have any value when storing the data
    @Temporal is used whenever Date is the type.  THis is because java stores it as a single object Date, but in the
    database, it can be either of the 3: (a) date, (b) time, or (c) date and time (i.e. timestamp).  Must match based
     on how the data table is defined
     */
    @Column(name = "birth_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date birthDate;
}
