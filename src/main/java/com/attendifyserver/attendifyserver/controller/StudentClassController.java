package com.attendifyserver.attendifyserver.controller;

import com.attendifyserver.attendifyserver.dto.AttendanceResponse;
import com.attendifyserver.attendifyserver.dto.ClassResponse;
import com.attendifyserver.attendifyserver.dto.ClassSessionResponse;
import com.attendifyserver.attendifyserver.dto.MessageResponse;
import com.attendifyserver.attendifyserver.service.ClassService;
import com.attendifyserver.attendifyserver.service.ClassSessionService;
import com.attendifyserver.attendifyserver.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/student")
@RequiredArgsConstructor
public class StudentClassController {

    private final ClassService classService;
    private final StudentService studentService;
    private final ClassSessionService classSessionService;

    @PostMapping("/class/joinClass")
    public ResponseEntity<?> joinClass(@RequestParam String classCode) {
        try {
            ClassResponse newClass = classService.joinClass(classCode);
            return ResponseEntity.ok(newClass);
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }


    @GetMapping("/class/getClasses")
    public ResponseEntity<?> getAllClasses() {
        try {
            List<ClassResponse> newClass = classService.getStudentClasses();
            return ResponseEntity.ok(newClass);
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }



    @GetMapping("/class/getAllClassSession")
    public ResponseEntity<?> getAllClassSession(@RequestParam("classId")Long classId) {
        try {
            List<ClassSessionResponse> newClass = classSessionService.getAllClassSessions(classId);
            return ResponseEntity.ok(newClass);
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }

    @GetMapping("/classSession/getAttendance")
    public ResponseEntity<?> getClassSessionsAttendance(@RequestParam("classSessionId")Long classSessionId,
                                                           @RequestParam("studentId")Long studentId) {
        try {
            List<AttendanceResponse> classSession = classSessionService.getAttendance(classSessionId,studentId);
            return ResponseEntity.ok(classSession);

        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }



}
