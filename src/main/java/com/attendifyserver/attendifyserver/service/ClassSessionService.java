package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionRequest;
import com.attendifyserver.attendifyserver.dto.SyncOfflineSessionResponse;
import com.attendifyserver.attendifyserver.entity.Attendance;
import com.attendifyserver.attendifyserver.entity.ClassSession;
import com.attendifyserver.attendifyserver.entity.Classes;
import com.attendifyserver.attendifyserver.repository.ClassRepository;
import com.attendifyserver.attendifyserver.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClassSessionService {

    private final AttendanceService attendanceService;
    private final ClassRepository classRepository;
    private final ClassSessionRepository sessionRepository;

    @Transactional
    public SyncOfflineSessionResponse processAndSaveOfflineSession(List<SyncOfflineSessionRequest> requestSession) {

        for (SyncOfflineSessionRequest request:requestSession){

            Classes targetClass = classRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Class not found. Cannot sync session."
                    ));

            System.out.println("Found class" + targetClass.getId());

            if (sessionRepository.existsByClassesIdAndSessionDate(targetClass.getId(),request.getSessionDate())){
                System.out.println("Duplicate Found (skipping)");
                continue;
            }

            ClassSession currentSession = ClassSession.builder()
                    .attendances(new ArrayList<Attendance>())
                    .classes(targetClass)
                    .sessionDate(request.getSessionDate())
                    .build();


            ClassSession savedClassSession=sessionRepository.save(currentSession);

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
}