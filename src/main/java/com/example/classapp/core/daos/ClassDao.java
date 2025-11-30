package com.example.classapp.core.daos;


import com.example.classapp.core.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassDao extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByCreatorId(Long creatorId);
    Optional<ClassEntity> findById(Long classId);
}
