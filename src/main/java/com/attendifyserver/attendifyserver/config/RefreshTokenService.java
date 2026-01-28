package com.attendifyserver.attendifyserver.config;

import com.attendifyserver.attendifyserver.entity.RefreshToken;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import com.attendifyserver.attendifyserver.repository.RefreshTokenRepository;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import com.attendifyserver.attendifyserver.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    @Transactional
    public RefreshToken createRefreshToken(String requestToken) {


        RefreshToken token = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh token not found!"));


        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.deleteByToken(token.getToken());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please login again.");
        }

        Student student = token.getStudent();
        Teacher teacher = token.getTeacher();

        refreshTokenRepository.delete(token);

        /// generate the token
        RefreshToken newRefreshToken = RefreshToken.builder()
                .student(student)
                .teacher(teacher)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .build();

        return refreshTokenRepository.save(newRefreshToken);
    }
}