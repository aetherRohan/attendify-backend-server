package com.attendifyserver.attendifyserver.repository;

import com.attendifyserver.attendifyserver.entity.RefreshToken;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);

    Optional<RefreshToken>findByTeacher(Teacher teacher);
    Optional<RefreshToken>findByStudent(Student student);
}
