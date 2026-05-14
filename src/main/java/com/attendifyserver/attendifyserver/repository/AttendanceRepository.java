package com.attendifyserver.attendifyserver.repository;

import com.attendifyserver.attendifyserver.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {


    Optional<Attendance> findByStudentIdAndClassSessionsId(Long studentId, Long classSessionsId);

    List<Attendance> findByClassSessionsId(Long classSessionsId);
    List<Attendance> findByClassSessionsIdAndStudentId(Long classSessionsId,Long studentId);

}
