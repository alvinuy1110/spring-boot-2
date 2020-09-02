package com.myproject.springboot.validation.domain;

import java.util.List;

import javax.validation.constraints.Size;

import com.myproject.springboot.validation.custom.ValidateEmployee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employer {

    private long id;
    
    @ValidateEmployee
    private Employee employee;
    
    private List<@ValidateEmployee Employee> employees;
//    private List< Employee> employees;

    @Size(
            min = 5,
            max = 14,
            message = "The employer firstName '${validatedValue}' must be between {min} and {max} characters long"
    )
    private String firstName;
    
}
