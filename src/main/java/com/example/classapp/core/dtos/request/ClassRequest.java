package com.example.classapp.core.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClassRequest {
    @NotBlank(message = "The class needs a name")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters long")
    private String name;
    @Size(max = 500, message = "The name cannot be longer than 500 characters")
    private String description;
}
