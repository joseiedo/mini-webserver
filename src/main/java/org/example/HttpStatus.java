package org.example;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ;

    final int code;
    final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
