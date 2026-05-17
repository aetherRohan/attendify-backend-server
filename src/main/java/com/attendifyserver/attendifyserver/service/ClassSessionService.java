package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.dto.AttendanceResponse;
import com.attendifyserver.attendifyserver.dto.ClassSessionResponse;
import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionRequest;
import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionResponse;
import com.attendifyserver.attendifyserver.entity.Attendance;
import com.attendifyserver.attendifyserver.entity.ClassSession;
import com.attendifyserver.attendifyserver.entity.Classes;
import com.attendifyserver.attendifyserver.repository.AttendanceRepository;
import com.attendifyserver.attendifyserver.repository.ClassRepository;
import com.attendifyserver.attendifyserver.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClassSessionService {

    private final AttendanceService attendanceService;
    private final ClassRepository classRepository;
    private final ClassSessionRepository classSessionRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public SyncOfflineSessionResponse processAndSaveOfflineSession(List<SyncOfflineSessionRequest> requestSession) {

        for (SyncOfflineSessionRequest request : requestSession) {

            Classes targetClass = classRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Class not found. Cannot sync session."
                    ));

            System.out.println("Found class" + targetClass.getId());

            if (classSessionRepository.existsByClassesIdAndSessionDate(targetClass.getId(), request.getSessionDate())) {
                System.out.println("Duplicate Found (skipping)");
                continue;
            }

            ClassSession currentSession = ClassSession.builder()
                    .attendances(new ArrayList<Attendance>())
                    .classes(targetClass)
                    .sessionDate(request.getSessionDate())
                    .build();


            ClassSession savedClassSession = classSessionRepository.save(currentSession);

            /// ATTENDANCE LOGIC HERE
            attendanceService.calculateAndSaveBulkAttendance(
                    request.getTotalWindowsCount(),
                    request.getStudentWindowCounts(),
                    savedClassSession, targetClass.getId());
        }
        return SyncOfflineSessionResponse.builder()
                .STATUS("success")
                .build();
    }


    public List<ClassSessionResponse> getAllClassSessions(Long classId) {

        if (!classRepository.existsById(classId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class doesn't exist");
        }

        List<ClassSession> rawClassSession = classSessionRepository.findByClassesId(classId);

        return rawClassSession.stream().map(classSession ->

                ClassSessionResponse.builder()
                        .classSessionId(classSession.getId())
                        .classId(classId)
                        .date(classSession.getSessionDate())
                        .build()
        ).collect(Collectors.toList());

    }


    public List<AttendanceResponse> getAllAttendanceForTeacher(Long sessionId) {

        if (!classSessionRepository.existsById(sessionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class-Session doesn't exist");
        }

        List<Attendance> rawAttendance = attendanceRepository.findByClassSessionsId(sessionId);

        Optional<Classes> curClassOpt = classRepository.findByClassSessionsId(sessionId);
        Optional<ClassSession> optionalClassSession = classSessionRepository.findById(sessionId);


        Classes classes = curClassOpt.orElseThrow(() ->
                new RuntimeException("Class not found with the provided ID")
        );

        ClassSession classSession = optionalClassSession.orElseThrow(() ->
                new RuntimeException("ClassSession not found with the provided ID")
        );

        return rawAttendance.stream().map(currentAttendance ->

                AttendanceResponse.builder()
                        .classId(classes.getId())
                        .classSessionId(sessionId)
                        .date(classSession.getSessionDate())
                        .studentId(currentAttendance.getStudent().getId())
                        .isPresent(currentAttendance.isPresent())
                        .studentName(currentAttendance.getStudent().getName())
                        .rollNumber(currentAttendance.getStudent().getRollNumber())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<AttendanceResponse> getAttendanceByStudentIdAndCLassId(Long classId, Long studentId) {

        if (!classRepository.existsById(classId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Class or Student doesn't exist");
        }

        List<Attendance> rawAttendance = attendanceRepository.findByStudentIdAndClassId(studentId, classId);

        return rawAttendance.stream().map(currentAttendance ->

                AttendanceResponse.builder()
                        .date(currentAttendance.getClassSessions().getSessionDate())
                        .classId(classId)
                        .classSessionId(currentAttendance.getClassSessions().getId())
                        .studentId(studentId)
                        .isPresent(currentAttendance.isPresent())
                        .studentName(currentAttendance.getStudent().getName())
                        .rollNumber(currentAttendance.getStudent().getRollNumber())
                        .build()
        ).collect(Collectors.toList());

    }

}