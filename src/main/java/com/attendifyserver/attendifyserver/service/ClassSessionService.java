package com.attendifyserver.attendifyserver.service;
import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionRequest;
import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionResponse;
import com.attendifyserver.attendifyserver.entity.ClassSession;
import com.attendifyserver.attendifyserver.entity.Classes;
import com.attendifyserver.attendifyserver.repository.ClassRepository;
import com.attendifyserver.attendifyserver.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class ClassSessionService {

    private final AttendanceService attendanceService;
    private final ClassRepository classRepository;
    private final ClassSessionRepository sessionRepository;

    @Transactional
    public SyncOfflineSessionResponse processAndSaveOfflineSession(SyncOfflineSessionRequest request) {

        Classes targetClass = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Class not found. Cannot sync session."
                ));

        System.out.println("Found class"+targetClass.getId());

        /// ATTENDANCE LOGIC HERE~~~~
      ClassSession savedSessionAttendance= attendanceService.calculateAndSaveBulkAttendance(
                                                                   request.getTotalWindowsCount(),
                                                                   request.getStudentWindowCounts(),
                                                                   request.getClassId(),
                                                                   request.getSessionDate(),targetClass);


       /// return the response that it synced perfectly
        return SyncOfflineSessionResponse.builder()
                .sessionId(savedSessionAttendance.getId())
                .classId(targetClass.getId())
                .status("success")
                .build();
    }
}