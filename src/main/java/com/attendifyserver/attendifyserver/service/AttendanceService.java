package com.attendifyserver.attendifyserver.service;
import com.attendifyserver.attendifyserver.entity.Attendance;
import com.attendifyserver.attendifyserver.entity.ClassSession;
import com.attendifyserver.attendifyserver.entity.Classes;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.repository.AttendanceRepository;
import com.attendifyserver.attendifyserver.repository.ClassSessionRepository;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final StudentRepository studentRepository;
    private final ClassSessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;

    private final int THRESHOLD = 60;


    public ClassSession calculateAndSaveBulkAttendance(int totalWindowCount,
                                                       Map<UUID, Integer> studentWindowCounts,
                                                       Long classId,
                                                       Date date,
                                                       Classes classes) {

        System.out.println("Attendance service entered");

        if (totalWindowCount < 1 || studentWindowCounts.size() < 1)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Not a Valid Data");


        ClassSession currentSession=ClassSession.builder()
                .attendances(new ArrayList<Attendance>())
                .classes(classes)
                .session_date(date)
                .build();

        ClassSession savedClassSession=sessionRepository.save(currentSession);

        System.out.println("saved classSession"+savedClassSession.getId()+"date:"+savedClassSession.getSession_date());

        List<Student>studentList=studentRepository.findByClassesId(classId);

        List<Attendance>attendanceList=new ArrayList<>();


/// MARKING ATTENDANCE FOR EACH STUDENT ENROLLED IN THE CLASS
        for (Student student:studentList){

            UUID bleUuid=student.getBleUuid();
            boolean isPresent=false;

            int windowCount=studentWindowCounts.getOrDefault(bleUuid,0);

            if (((double) windowCount / totalWindowCount)*100 >= THRESHOLD) isPresent=true;

            Attendance attendance = Attendance.builder()
                                              .classSessions(savedClassSession)
                                              .isPresent(isPresent)
                                              .student(student)
                                              .build();
            attendanceList.add(attendance);
        }
        List<Attendance> savedAttendance=attendanceRepository.saveAll(attendanceList);

        currentSession.getAttendances().addAll(savedAttendance);
        return sessionRepository.save(currentSession);
    }
}
