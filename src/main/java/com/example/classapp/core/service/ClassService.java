package com.example.classapp.core.service;


import com.example.classapp.core.dtos.request.ClassRequest;
import com.example.classapp.core.dtos.response.ClassResponse;
import com.example.classapp.core.dtos.response.StudentResponse;
import com.example.classapp.shared.dtos.GlobalResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClassService {
    ResponseEntity<GlobalResponse<ClassResponse>> createClass(ClassRequest request);
    ResponseEntity<GlobalResponse<List<ClassResponse>>> getAllClasses();
    ResponseEntity<GlobalResponse<List<ClassResponse>>> getMyCreatedClasses();
    ResponseEntity<GlobalResponse<List<StudentResponse>>> getClassStudents(Long classId);
    ResponseEntity<GlobalResponse<ClassResponse>> findClass(Long classId);
    ResponseEntity<GlobalResponse<ClassResponse>> updateClass(Long classId,ClassRequest request);
    ResponseEntity<GlobalResponse<Void>> deleteClass(Long classId);
}
