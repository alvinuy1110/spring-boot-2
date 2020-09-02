package com.myproject.springboot.web.api;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Having a mapping between Java enum type to text allows us to refactor and rename
 * the Java enum without affecting client code that relies on the text type to do UI logic.
 */
public enum ApiErrorType {
    DOES_NOT_EXIST("DOES_NOT_EXIST"),
    // Form Validation Errors
    INVALID_INPUT("INVALID_INPUT"),
    // Server Errors
    FAILED_PERSISTENCE("FAILED_PERSISTENCE"),
    SYSTEM_ERROR("SYSTEM_ERROR");

    private final String text;

    ApiErrorType(String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}