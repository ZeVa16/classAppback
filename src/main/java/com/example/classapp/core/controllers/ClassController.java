package com.example.classapp.core.controllers;

import com.example.classapp.core.dtos.request.ClassRequest;
import com.example.classapp.core.dtos.response.ClassResponse;
import com.example.classapp.core.dtos.response.StudentResponse;
import com.example.classapp.core.service.ClassService;
import com.example.classapp.shared.constants.Api;
import com.example.classapp.shared.dtos.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Api.API_V1+Api.CLASS)
@RequiredArgsConstructor
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping("/create")
    public ResponseEntity<GlobalResponse<ClassResponse>> createClass(@Valid @RequestBody ClassRequest classRequest) {
        return classService.createClass(classRequest);
    }

    @GetMapping("/get-all")
    public ResponseEntity<GlobalResponse<List<ClassResponse>>> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/find-class/{classId}")
    public ResponseEntity<GlobalResponse<ClassResponse>> findClass(@PathVariable Long classId) {
        return classService.findClass(classId);
    }

    @GetMapping("/my-classes")
    public ResponseEntity<GlobalResponse<List<ClassResponse>>> getMyClasses() {
        return classService.getMyCreatedClasses();
    }

    @GetMapping("/{classId}/students")
    public ResponseEntity<GlobalResponse<List<StudentResponse>>> getStudents(@PathVariable Long classId) {
        return classService.getClassStudents(classId);
    }

    @PutMapping("/update/{classId}")
    public ResponseEntity<GlobalResponse<ClassResponse>> updateClass(@PathVariable Long classId, @RequestBody ClassRequest classRequest) {
        return classService.updateClass(classId, classRequest);
    }
    @DeleteMapping("/delete/{classId}")
    public ResponseEntity<GlobalResponse<Void>> deleteClass(@PathVariable Long classId) {
        return classService.deleteClass(classId);
    }



}
