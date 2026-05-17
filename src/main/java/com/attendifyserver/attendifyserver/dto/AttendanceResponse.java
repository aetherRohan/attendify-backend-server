package com.attendifyserver.attendifyserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceResponse {

    private Long classSessionId;
    private Long classId;
    private Long studentId;
    private String date;
    private String studentName;
    private Integer rollNumber;
    private Boolean isPresent;

}
