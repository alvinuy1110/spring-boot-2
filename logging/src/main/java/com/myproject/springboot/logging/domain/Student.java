package com.myproject.springboot.logging.domain;

import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {

    private long id;

    @NotEmpty
    @Size(max = 20)
    private String firstName;
    
    @NotBlank
    private String lastName;

    @NotNull
    @Past
    private Date birthDate;
    
 
    
    @Positive
    private int numberOfSubjects;
    
    
    private boolean married;
    
    @NotEmpty
    @Email
    private String email;
    

    // means the contents are valid;  null list is allowed
    private List<@NotBlank String> classes;
    
    
}
