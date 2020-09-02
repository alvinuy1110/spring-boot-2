package com.myproject.springboot.web.api;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Soft errors for the Web-API's clients to use.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorDto {

    private ApiError error; //NOSONAR
}