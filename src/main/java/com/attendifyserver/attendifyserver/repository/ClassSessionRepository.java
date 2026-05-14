package com.attendifyserver.attendifyserver.repository;

import com.attendifyserver.attendifyserver.entity.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassSessionRepository extends JpaRepository<ClassSession,Long> {

    boolean existsByClassesIdAndSessionDate(Long classId, String sessionDate);

    List<ClassSession> findByClassesId(Long classId);


}
