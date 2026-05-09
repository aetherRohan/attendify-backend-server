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



    @Transactional
    public RefreshToken createRefreshToken(String requestToken) {


        RefreshToken token = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh token not found!"));


        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please login again.");
        }


        /// update the token
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));

        return refreshTokenRepository.save(token);
    }
}