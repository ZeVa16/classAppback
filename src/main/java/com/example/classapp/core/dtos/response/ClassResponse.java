package com.example.classapp.core.dtos.response;


import com.example.classapp.core.entity.TeacherEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ClassResponse {

    private Long id;
    private String name;
    private String description;
    private String creatorName;
    private LocalDateTime createdAt;
    private Long studentsEnrolled;
    private Boolean isEnrolled;


}
