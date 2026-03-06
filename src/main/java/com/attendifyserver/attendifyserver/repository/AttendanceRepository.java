package com.attendifyserver.attendifyserver.repository;

import com.attendifyserver.attendifyserver.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
}
