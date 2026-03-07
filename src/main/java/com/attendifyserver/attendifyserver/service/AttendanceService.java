package com.attendifyserver.attendifyserver.service;
import com.attendifyserver.attendifyserver.dto.StudentAttendanceResponse;
import com.attendifyserver.attendifyserver.entity.Attendance;
import com.attendifyserver.attendifyserver.entity.ClassSession;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.repository.AttendanceRepository;
import com.attendifyserver.attendifyserver.repository.ClassSessionRepository;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final StudentRepository studentRepository;
    private final ClassSessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;

    private final int THRESHOLD = 60;


    public List<StudentAttendanceResponse> calculateAndSaveBulkAttendance(int totalWindowCount,
                                                                          Map<UUID, Integer> studentWindowCounts,
                                                                          ClassSession currentSession, long classId) {

        System.out.println("Attendance service entered");

        if (totalWindowCount < 1 || studentWindowCounts.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sync data provided");


        List<Student> studentList = studentRepository.findByClassesId(classId);
        System.out.println("Found Students enrolled in the class");

        List<Attendance> attendanceList = new ArrayList<>();
        List<StudentAttendanceResponse> attendanceResponseList = new ArrayList<>();


         /// MARKING ATTENDANCE FOR EACH STUDENT ENROLLED IN THE CLASS
        for (Student student:studentList){

            UUID bleUuid=student.getBleUuid();
            boolean isPresent=false;

            int windowCount=studentWindowCounts.getOrDefault(bleUuid,0);

            if (((double) windowCount / totalWindowCount)*100 >= THRESHOLD) isPresent=true;

            Attendance attendance = Attendance.builder()
                                              .classSessions(currentSession)
                                              .isPresent(isPresent)
                                              .student(student)
                                              .build();
            System.out.println("Attendance Calculated");
            System.out.println(student.getRollNumber());

            StudentAttendanceResponse response = StudentAttendanceResponse.builder()
                    .rollNumber(student.getRollNumber())
                    .studentName(student.getName())
                    .isPresent(isPresent)
                    .build();
            System.out.println("Response DTO calculated");

            attendanceList.add(attendance);
            attendanceResponseList.add(response);
        }
        List<Attendance> savedAttendance = attendanceRepository.saveAll(attendanceList);

        currentSession.getAttendances().addAll(savedAttendance);
        sessionRepository.save(currentSession);
        System.out.println("Session Saved");
        return attendanceResponseList;
    }
}
