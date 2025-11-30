package com.example.classapp.core.daos;

import com.example.classapp.core.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentsDao extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByUserId(Long userId);
}
