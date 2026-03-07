package com.attendifyserver.attendifyserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentAttendanceResponse {
    private String studentName;
    private int rollNumber;
    private boolean isPresent;
}
