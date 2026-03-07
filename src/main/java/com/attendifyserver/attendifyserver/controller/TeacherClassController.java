package com.attendifyserver.attendifyserver.controller;
import com.attendifyserver.attendifyserver.dto.*;
import com.attendifyserver.attendifyserver.service.ClassService;
import com.attendifyserver.attendifyserver.service.ClassSessionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherClassController {

    private final ClassService classService;
    private final ClassSessionService sessionService;


    @PostMapping("/class")
    public ResponseEntity<?> createClass(@RequestBody CreateClassRequest classRequest) {
        try {
            ClassResponse newClass = classService.createClass(classRequest);
            return ResponseEntity.ok(newClass);
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }

    @GetMapping("/class/{classId}/students")
    public ResponseEntity<?> getEnrolledStudents(@PathVariable Long classId) {
        try {
            List<StudentResponse> studentList = classService.getStudentsForClass(classId);
            return ResponseEntity.ok(studentList);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }

    @GetMapping("/class/getClasses")
    public ResponseEntity<?> getAllClasses() {
        try {
            List<ClassResponse> newClass = classService.getTeacherClasses();
            return ResponseEntity.ok(newClass);
        } catch (ResponseStatusException e) {

            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }

    @PostMapping("/session/sync")
    public ResponseEntity<?> syncClassSession(@RequestBody SyncOfflineSessionRequest syncRequest) {
        System.out.println("session controller entered");
        try {
            System.out.println("trying to call service to save attendance");
            SyncOfflineSessionResponse response = sessionService.processAndSaveOfflineSession(syncRequest);

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse(e.getMessage()));
        }
    }
}