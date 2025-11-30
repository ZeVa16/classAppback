package com.example.classapp.core.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;



@Getter
@Setter
@Builder
public class EnrollmentResponse {

    private Long id;
    private Long classId;
    private String className;
    private String classDescription;
    private String teacherName;
    private LocalDateTime enrollmentDate;

}
