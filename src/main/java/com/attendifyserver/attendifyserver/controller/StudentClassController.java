package com.attendifyserver.attendifyserver.controller;

import com.attendifyserver.attendifyserver.dto.ClassResponse;
import com.attendifyserver.attendifyserver.dto.MessageResponse;
import com.attendifyserver.attendifyserver.service.ClassService;
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

    @PostMapping("/class")
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
            List<ClassResponse> newClass = classService.getAllClasses();
            return ResponseEntity.ok(newClass);
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }
}
