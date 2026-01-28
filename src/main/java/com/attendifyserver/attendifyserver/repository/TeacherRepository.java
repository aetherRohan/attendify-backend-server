package com.attendifyserver.attendifyserver.repository;


import com.attendifyserver.attendifyserver.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(@Param("email") String email);

    Boolean existsByEmail(@Param("email") String email);
}
