package com.example.classapp.shared.exceptions;


import com.example.classapp.shared.dtos.GlobalResponse;
import com.example.classapp.shared.utils.ResponseGenerator;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<GlobalResponse<Void>> handle(GlobalException ex) {
        return ResponseGenerator.generateResponse(ex.getMessage(), ex.getHttpStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GlobalResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseGenerator.generateResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GlobalResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseGenerator.generateResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
