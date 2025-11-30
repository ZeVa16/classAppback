package com.example.classapp.shared.utils;

import com.example.classapp.shared.dtos.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ResponseGenerator {

    public static <T>ResponseEntity<GlobalResponse<T>> generateResponse(String message, T data, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(GlobalResponse.<T>builder()
                        .message(message)
                        .data(data)
                        .status(status)
                        .code(status.value())
                        .build());
    }

    public static ResponseEntity<GlobalResponse<Void>> generateResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(GlobalResponse.<Void>builder()
                        .message(message)
                        .status(status)
                        .code(status.value())
                        .data(null)
                        .build());
    }
}
