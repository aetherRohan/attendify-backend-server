package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.config.RefreshTokenService;
import com.attendifyserver.attendifyserver.dto.*;
import com.attendifyserver.attendifyserver.entity.RefreshToken;
import com.attendifyserver.attendifyserver.enums.Roles;
import lombok.RequiredArgsConstructor;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

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
        String jwtToken = jwtService.generateToken(new CustomUserDetails(savedStudent));

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(UUID.randomUUID().toString())
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
        String jwtToken = jwtService.generateToken(new CustomUserDetails(savedTeacher));

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(UUID.randomUUID().toString())
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
        String refreshToken = UUID.randomUUID().toString();

        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(userDetails.getAuthorities().toString())
                .name(userDetails.getName())
                .userId(userDetails.getUserId())
                .bleUuid(userDetails.getBleUuid())
                .build();
    }


    @Transactional
    public LoginResponse createRefreshToken(RefreshTokenRequest request) {

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

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .role(userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .name(userDetails.getName())
                .userId(userDetails.getUserId())
                .build();
    }
}