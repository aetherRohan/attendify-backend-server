package com.attendifyserver.attendifyserver.controller;


import com.attendifyserver.attendifyserver.dto.LoginRequest;
import com.attendifyserver.attendifyserver.dto.LoginResponse;
import com.attendifyserver.attendifyserver.dto.MessageResponse;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import com.attendifyserver.attendifyserver.enums.Roles;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import com.attendifyserver.attendifyserver.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class authController {


    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;


    @PostMapping("/signup/student")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) {

        try {
            if (studentRepository.existsByEmail(student.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Email Already in use"));
            }
            Student savedStudent = studentRepository.save(student);
            return ResponseEntity.ok(new LoginResponse("SUCCESS",
                    Roles.ROLE_STUDENT.name(),
                    savedStudent.getId(),
                    savedStudent.getName(), "token"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }

    @PostMapping("/signup/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody Teacher teacher) {
        try {
            if (teacherRepository.existsByEmail(teacher.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Email Already in use"));
            }
            Teacher savedTeacher = teacherRepository.save(teacher);
            return ResponseEntity.ok(new LoginResponse("SUCCESS",
                    Roles.ROLE_TEACHER.name(),
                    savedTeacher.getId(),
                    savedTeacher.getName(), "token"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {

            Optional<Student> studentOPT = studentRepository.findByEmail(loginRequest.getEmail());
            if (studentOPT.isPresent()) {
                Student getStudent = studentOPT.get();
                if (getStudent.getPassword().equals(loginRequest.getPassword())) {
                    return ResponseEntity.ok(new LoginResponse("SUCCESS",
                            Roles.ROLE_STUDENT.name(),
                            getStudent.getId(),
                            getStudent.getName(), "token"));
                } else return ResponseEntity.badRequest().body(new MessageResponse("Wrong Password"));
            }

            Optional<Teacher> teacherOPT = teacherRepository.findByEmail(loginRequest.getEmail());
            if (teacherOPT.isPresent()) {
                Teacher getTeacher = teacherOPT.get();
                if (getTeacher.getPassword().equals(loginRequest.getPassword())) {
                    return ResponseEntity.ok(new LoginResponse("SUCCESS",
                            Roles.ROLE_TEACHER.name(),
                            getTeacher.getId(),
                            getTeacher.getName(), "token"));
                } else return ResponseEntity.badRequest().body(new MessageResponse("Wrong Password"));
            }
            return ResponseEntity.status(404).body(new MessageResponse("User not found !"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }


}
