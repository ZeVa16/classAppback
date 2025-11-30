package com.example.classapp.core.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class StudentResponse {

    private Long id;
    private String name;
    private LocalDateTime enrollmentDate;
}
