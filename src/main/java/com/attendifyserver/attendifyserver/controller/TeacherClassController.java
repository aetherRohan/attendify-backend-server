package com.attendifyserver.attendifyserver.controller;

import com.attendifyserver.attendifyserver.dto.*;
import com.attendifyserver.attendifyserver.service.ClassService;
import com.attendifyserver.attendifyserver.service.ClassSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

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





    /**
     * 3. SYNC OFFLINE SESSION (The Heavy Lifter)
     * Called by Android WorkManager when the class ends and internet is restored.
     * This creates the ClassSession AND saves all Attendance records in one transaction.
     */
    @PostMapping("/sessions/sync")
    public ResponseEntity<?> syncClassSession(@RequestBody SyncOfflineSessionRequest syncRequest) {

        // This service method will calculate the 60% threshold for each student in the Map
        sessionService.processAndSaveOfflineSession(syncRequest);

        return ResponseEntity.ok(Map.of(
                "message", "Offline session and attendance synced successfully!"
        ));
    }
}