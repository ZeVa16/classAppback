package com.example.classapp.core.service;

import com.example.classapp.core.dtos.response.EnrollmentResponse;
import com.example.classapp.shared.dtos.GlobalResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EnrollmentService {
    ResponseEntity<GlobalResponse<EnrollmentResponse>> enrollIn(Long classId);
    ResponseEntity<GlobalResponse<List<EnrollmentResponse>>> getMyEnrollments();
}
