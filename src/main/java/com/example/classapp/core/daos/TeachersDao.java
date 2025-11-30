package com.example.classapp.core.daos;

import com.example.classapp.core.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeachersDao extends JpaRepository<TeacherEntity, Long> {
    Optional<TeacherEntity> findByUserId(Long userId);
}
