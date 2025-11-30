package com.example.classapp.shared.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse<T> {

    private String message;
    private T data;
    private HttpStatus status;
    private int code;

}
