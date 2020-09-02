package com.myproject.springboot.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.myproject.springboot.web.api.ApiError;
import com.myproject.springboot.web.api.ApiErrorDto;
import com.myproject.springboot.web.api.ApiErrorType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug("Illegal argument error", e);
        return new ApiErrorDto(new ApiError(ApiErrorType.INVALID_INPUT, e.getMessage()));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MissingPathVariableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorDto handleMissingData(Exception e) {
        log.debug("Data integrity error", e);
        return new ApiErrorDto(new ApiError(ApiErrorType.INVALID_INPUT, "Missing data: " + e.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiErrorDto handleWSProcessingException(Exception e) {
        log.error("System error", e);
        return new ApiErrorDto(new ApiError(ApiErrorType.SYSTEM_ERROR, e.getMessage()));
    }

}
