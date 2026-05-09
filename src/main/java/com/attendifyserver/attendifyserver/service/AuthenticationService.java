package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.config.RefreshTokenService;
import com.attendifyserver.attendifyserver.dto.*;
import com.attendifyserver.attendifyserver.entity.RefreshToken;
import com.attendifyserver.attendifyserver.enums.Roles;
import com.attendifyserver.attendifyserver.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.attendifyserver.attendifyserver.config.CustomUserDetails;
import com.attendifyserver.attendifyserver.config.JwtService;
import com.attendifyserver.attendifyserver.dto.LoginResponse;
import com.attendifyserver.attendifyserver.dto.LoginRequest;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import com.attendifyserver.attendifyserver.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpirationMs;

    @Transactional
    public LoginResponse registerStudent(SignupRequest request) {

        if (studentRepository.existsByEmail(request.getEmail()) ||
                teacherRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        Student savedStudent = studentRepository.save(student);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .student(savedStudent)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .build();

        refreshTokenRepository.save(newRefreshToken);

        String jwtToken = jwtService.generateToken(new CustomUserDetails(savedStudent));

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(newRefreshToken.getToken())
                .name(savedStudent.getName())
                .role(Roles.STUDENT.name())
                .userId(savedStudent.getId())
                .bleUuid(savedStudent.getBleUuid())
                .build();
    }

    @Transactional
    public LoginResponse registerTeacher(SignupRequest request) {

        if (studentRepository.existsByEmail(request.getEmail()) ||
                teacherRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }


        Teacher teacher = Teacher.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();


        Teacher savedTeacher = teacherRepository.save(teacher);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .teacher(savedTeacher)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .build();

        refreshTokenRepository.save(newRefreshToken);

        String jwtToken = jwtService.generateToken(new CustomUserDetails(savedTeacher));

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(newRefreshToken.getToken())
                .name(savedTeacher.getName())
                .role(Roles.TEACHER.name())
                .userId(savedTeacher.getId())
                .build();
    }


    public LoginResponse login(LoginRequest request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);


        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        long userId = userDetails.getUserId();

        RefreshToken refreshToken;

        //  "Update or Create" the Refresh Token based on Role
        if (role.equals("TEACHER")) {
            Teacher teacher = teacherRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            // Find existing token, or create a  new one if it doesn't exist
            refreshToken = refreshTokenRepository.findByTeacher(teacher)
                    .orElse(new RefreshToken());

            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
            refreshToken.setTeacher(teacher);

        } else {
            // It's a STUDENT
            Student student = studentRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            refreshToken = refreshTokenRepository.findByStudent(student)
                    .orElse(new RefreshToken());

            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
            refreshToken.setStudent(student);
        }

        refreshTokenRepository.save(refreshToken);

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .role(userDetails.getAuthorities().toString())
                .name(userDetails.getName())
                .userId(userDetails.getUserId())
                .bleUuid(userDetails.getBleUuid())
                .build();
    }


    @Transactional
    public TokenResponse createRefreshToken(RefreshTokenRequest request) {

        // 1. Perform Rotation (Verify Old -> Delete Old -> Get New)
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(request.getRefreshToken());

        // 2. Get User Details from the NEW token
        CustomUserDetails userDetails;
        if (newRefreshToken.getStudent() != null) {
            userDetails = new CustomUserDetails(newRefreshToken.getStudent());
        } else {
            userDetails = new CustomUserDetails(newRefreshToken.getTeacher());
        }

        // 3. Generate NEW Access Token
        String newAccessToken = jwtService.generateToken(userDetails);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }



}