package com.attendifyserver.attendifyserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponse {
    private Long studentId;
    private Long classId;
    private String name;
    private Integer rollNumber;
    private String bleUuid;
}