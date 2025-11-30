package com.example.classapp.core.daos;

import com.example.classapp.core.entity.EnrollmentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentDao extends JpaRepository<EnrollmentsEntity, Long> {
    Optional<EnrollmentsEntity> findByStudentIdAndClassEntityId(Long studentId, Long classEntityId);
    boolean existsByStudentIdAndClassEntityId(Long studentId, Long classEntityId);
    List<EnrollmentsEntity> findByStudentId(Long studentId);
    List<EnrollmentsEntity> findByClassEntityId(Long classEntityId);
    Long countByStudentId(Long studentId);

}
