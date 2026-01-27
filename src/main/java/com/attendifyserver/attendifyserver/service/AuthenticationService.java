package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.dto.*;
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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
                .token(jwtToken)
                .name(savedStudent.getName())
                .role(Roles.ROLE_STUDENT.name())
                .userId(savedStudent.getId())
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
                .token(jwtToken)
                .name(savedTeacher.getName())
                .role(Roles.ROLE_TEACHER.name())
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

        return LoginResponse.builder()
                .token(jwtToken)
                .role(userDetails.getAuthorities().toString())
                .name(userDetails.getName())
                .userId(userDetails.getUserId())
                .build();
    }
}