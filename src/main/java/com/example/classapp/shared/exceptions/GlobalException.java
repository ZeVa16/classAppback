package com.example.classapp.shared.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final HttpStatusCode httpStatusCode;

    public GlobalException(HttpStatus status, String message) {
        super(message);
        this.httpStatus = status;
        this.httpStatusCode = status;
    }
}
