package com.myproject.springboot.web.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Soft errors for the Web-API's clients to use.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private ApiErrorType type; //NOSONAR
    private String message; //NOSONAR

    @JsonProperty(value = "detail")
    private Object detailObjmessage; //NOSONAR

    public ApiError(ApiErrorType apiErrorType, String message) {
        this.type = apiErrorType;
        this.message = message;

    }
}