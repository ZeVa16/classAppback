package com.example.classapp.core.controllers;

import com.example.classapp.core.dtos.response.EnrollmentResponse;
import com.example.classapp.core.service.EnrollmentService;
import com.example.classapp.shared.constants.Api;
import com.example.classapp.shared.dtos.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Api.API_V1+Api.ENROLLMENT)
public class EnrollController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{classId}")
    public ResponseEntity<GlobalResponse<EnrollmentResponse>> enrollment(@PathVariable Long classId) {
        return enrollmentService.enrollIn(classId);
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<GlobalResponse<List<EnrollmentResponse>>> getMyEnrollments() {
        return enrollmentService.getMyEnrollments();
    }

}
