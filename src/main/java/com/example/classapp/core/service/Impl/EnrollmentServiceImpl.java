package com.example.classapp.core.service.Impl;

import com.example.classapp.core.daos.ClassDao;
import com.example.classapp.core.daos.EnrollmentDao;
import com.example.classapp.core.daos.StudentsDao;
import com.example.classapp.core.daos.UserDao;
import com.example.classapp.core.dtos.response.EnrollmentResponse;
import com.example.classapp.core.entity.ClassEntity;
import com.example.classapp.core.entity.EnrollmentsEntity;
import com.example.classapp.core.entity.StudentEntity;
import com.example.classapp.core.entity.UserEntity;
import com.example.classapp.core.service.EnrollmentService;
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
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentDao enrollmentDao;

    @Autowired
    private StudentsDao studentsDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ClassDao classDao;

    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.findByEmail(email)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,"User Not Found"));
    }
    private StudentEntity getCurrentStudent(String exMessage) {
        UserEntity user = getCurrentUser();
        return studentsDao.findByUserId(user.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,exMessage));
    }

    private ClassEntity getCurrentClass(Long classId) {
        return classDao.findById(classId)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND,"Class not found"));
    }

    @Transactional
    public ResponseEntity<GlobalResponse<EnrollmentResponse>> enrollIn(Long classId){

        StudentEntity student = getCurrentStudent("Only students can enroll in a class");

        ClassEntity classEntity = getCurrentClass(classId);

        if(enrollmentDao.existsByStudentIdAndClassEntityId(student.getId(), classId)){
            throw new GlobalException(HttpStatus.CONFLICT,"You are alredy in this class");
        }

        EnrollmentsEntity enrollment = new EnrollmentsEntity();
        enrollment.setStudent(student);
        enrollment.setClassEntity(classEntity);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollmentDao.save(enrollment);

        EnrollmentResponse response = EnrollmentResponse.builder()
                .id(enrollment.getId())
                .classId(classEntity.getId())
                .className(classEntity.getName())
                .classDescription(classEntity.getDescription())
                .teacherName(classEntity.getCreator().getUser().getName())
                .enrollmentDate(enrollment.getEnrolledAt())
                .build();

        return ResponseGenerator.generateResponse("Your are in this class now",response,HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<GlobalResponse<List<EnrollmentResponse>>> getMyEnrollments(){
        StudentEntity student = getCurrentStudent("Only students can see his enrollments");

        List<EnrollmentsEntity> enrollments = enrollmentDao.findByStudentId(student.getId());
        List<EnrollmentResponse> response = enrollments.stream()
                .map(e->{
                    return EnrollmentResponse.builder()
                            .id(e.getId())
                            .classId(e.getClassEntity().getId())
                            .className(e.getClassEntity().getName())
                            .classDescription(e.getClassEntity().getDescription())
                            .teacherName(e.getClassEntity().getCreator().getUser().getName())
                            .enrollmentDate(e.getEnrolledAt())
                            .build();
                }).collect(Collectors.toList());
        return ResponseGenerator.generateResponse("Your are in this class now",response,HttpStatus.OK);
    }

}
