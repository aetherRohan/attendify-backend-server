package com.attendifyserver.attendifyserver.repository;

import com.attendifyserver.attendifyserver.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {


    Optional<Attendance> findByStudentIdAndClassSessionsId(Long studentId, Long classSessionsId);

    List<Attendance> findByClassSessionsId(Long classSessionsId);
    List<Attendance> findByClassSessionsIdAndStudentId(Long classSessionsId,Long studentId);

    @Query("SELECT a FROM Attendance a " +
            "WHERE a.student.id = :studentId " +
            "AND a.classSessions.classes.id = :classId")
    List<Attendance> findByStudentIdAndClassId(
            @Param("studentId") Long studentId,
            @Param("classId") Long classId
    );

}
