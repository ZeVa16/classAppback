package com.example.classapp.core.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TeacherResponse {
    private Long id;
    private String name;
}
