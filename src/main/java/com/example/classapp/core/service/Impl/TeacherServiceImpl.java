package com.example.classapp.core.service.Impl;

import com.example.classapp.core.daos.ClassDao;
import com.example.classapp.core.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private ClassDao classDao;


//    @Transactional
//    public ResponseEntity<GlobalResponse<ClassResponse>> createClass(ClassRequest request){
//
//    }



}
