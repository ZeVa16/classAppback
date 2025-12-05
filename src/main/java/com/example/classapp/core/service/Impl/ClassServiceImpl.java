package com.example.classapp.core.service.Impl;

import com.example.classapp.core.daos.*;
import com.example.classapp.core.dtos.request.ClassRequest;
import com.example.classapp.core.dtos.response.ClassResponse;
import com.example.classapp.core.dtos.response.StudentResponse;
import com.example.classapp.core.entity.*;
import com.example.classapp.core.service.ClassService;
import com.example.classapp.shared.dtos.GlobalResponse;
import com.example.classapp.shared.exceptions.GlobalException;
import com.example.classapp.shared.utils.ResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassDao classDao;
    @Autowired
    private TeachersDao teachersDao;
    @Autowired
    private StudentsDao studentsDao;
    @Autowired
    private EnrollmentDao enrollmentDao;
    @Autowired
    private UserDao userDao;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.findByEmail(email)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,"User not found"));
    }

    private TeacherEntity getCurrentTeacher() {
        UserEntity user = getCurrentUser();
        return teachersDao.findByUserId(user.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,"Teacher not found"));
    }

    private StudentEntity getCurrentStudent() {
        UserEntity user = getCurrentUser();
        return studentsDao.findByUserId(user.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,"Student not found"));
    }

    private ClassEntity getCurrentClass(Long classId) {
        return classDao.findById(classId)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,"Class not found"));
    }


    @Transactional
    public ResponseEntity<GlobalResponse<ClassResponse>> createClass(ClassRequest request) {
        TeacherEntity teacher = getCurrentTeacher();

        ClassEntity classEntity = new ClassEntity();
        classEntity.setName(request.getName());
        classEntity.setDescription(request.getDescription());
        classEntity.setCreator(teacher);
        classEntity.setCreatedAt(LocalDateTime.now());
        classDao.save(classEntity);

        ClassResponse response = ClassResponse.builder()
                .id(classEntity.getId())
                .name(classEntity.getName())
                .description(classEntity.getDescription())
                .creatorName(teacher.getUser().getName())
                .createdAt(classEntity.getCreatedAt())
                .build();
        return ResponseGenerator.generateResponse("Class created succesfully",response,HttpStatus.CREATED);
    };

    @Transactional
    public ResponseEntity<GlobalResponse<ClassResponse>> updateClass(Long classId,ClassRequest request) {
        TeacherEntity teacher = getCurrentTeacher();

        ClassEntity classEntity = getCurrentClass(classId);

        if (!classEntity.getCreator().getId().equals(teacher.getId())) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "Only the creator can update this class");
        }

        classEntity.setName(request.getName());
        classEntity.setDescription(request.getDescription());
        classDao.save(classEntity);

        ClassResponse response = ClassResponse.builder()
                .id(classEntity.getId())
                .name(classEntity.getName())
                .description(classEntity.getDescription())
                .creatorName(teacher.getUser().getName())
                .build();
        return ResponseGenerator.generateResponse("Class updated succesfully",response,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<GlobalResponse<Void>> deleteClass(Long classId) {
        TeacherEntity teacher = getCurrentTeacher();

        ClassEntity classEntity = getCurrentClass(classId);

        if (!classEntity.getCreator().getId().equals(teacher.getId())) {
            throw new GlobalException(HttpStatus.FORBIDDEN, "Only the creator can delete this class");
        }

        classDao.delete(classEntity);
        return ResponseGenerator.generateResponse("Class deleted succesfully",null,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<GlobalResponse<List<ClassResponse>>> getAllClasses() {
        UserEntity user = getCurrentUser();
        List<ClassEntity> classes = classDao.findAll();
        StudentEntity student = null;
        if (user.getUserType().name().equals("STUDENT")) {
            student = studentsDao.findByUserId(user.getId())
                    .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Student not found"));
        }
        final StudentEntity finalStudent = student;
        List<ClassResponse> classResponses = classes.stream()
                    .map(c -> {
                        boolean isEnrolled = false;
                        if(finalStudent != null) {
                            isEnrolled=enrollmentDao.existsByStudentIdAndClassEntityId(finalStudent.getId(),c.getId());
                        }
                        return ClassResponse.builder()
                                .id(c.getId())
                                .name(c.getName())
                                .description(c.getDescription())
                                .creatorName(c.getCreator().getUser().getName())
                                .createdAt(c.getCreatedAt())
                                .isEnrolled(isEnrolled)
                                .build();
                    }).collect(Collectors.toList());
            return ResponseGenerator.generateResponse("All classes found", classResponses, HttpStatus.OK);
        };

    @Transactional
    public ResponseEntity<GlobalResponse<ClassResponse>> findClass(Long classId) {
       ClassEntity classEntity = getCurrentClass(classId);

        ClassResponse response = ClassResponse.builder()
                .id(classEntity.getId())
                .name(classEntity.getName())
                .description(classEntity.getDescription())
                .creatorName(classEntity.getCreator().getUser().getName())
                .createdAt(classEntity.getCreatedAt())
                .build();
        return ResponseGenerator.generateResponse("Class found", response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<GlobalResponse<List<ClassResponse>>> getMyCreatedClasses(){
        TeacherEntity teacher = getCurrentTeacher();

        List<ClassEntity> classes = classDao.findByCreatorId(teacher.getId());

        List<ClassResponse> response = classes.stream()
                .map(classEntity -> ClassResponse.builder()
                        .id(classEntity.getId())
                        .name(classEntity.getName())
                        .description(classEntity.getDescription())
                        .creatorName(classEntity.getCreator().getUser().getName())
                        .createdAt(classEntity.getCreatedAt())
                        .studentsEnrolled(classEntity.getEnrollments().stream().count())
                        .build()
                ).collect(Collectors.toList());
        return ResponseGenerator.generateResponse("All classes found", response, HttpStatus.OK);
    };

    @Transactional
    public ResponseEntity<GlobalResponse<List<StudentResponse>>> getClassStudents(Long classId) {
        TeacherEntity teacher = getCurrentTeacher();

        ClassEntity classEntity = getCurrentClass(classId);

        if(!classEntity.getCreator().getId().equals(teacher.getId())) {
            throw new GlobalException(HttpStatus.FORBIDDEN,"Only the creator can see this students");
        }

        List<EnrollmentsEntity> enrollments = enrollmentDao.findByClassEntityId(classEntity.getId());

        List<StudentResponse> response = enrollments.stream()
                .map(e -> StudentResponse.builder()
                        .id(e.getStudent().getId())
                        .name(e.getStudent().getUser().getName())
                        .enrollmentDate(e.getEnrolledAt())
                        .build()
                ).collect(Collectors.toList());
        return ResponseGenerator.generateResponse("All students found", response, HttpStatus.OK);
    }
}
