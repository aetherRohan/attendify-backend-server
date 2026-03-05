package com.attendifyserver.attendifyserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentResponse {
    private Long id;
    private String name;
    private String rollNumber;
    private String bleUuid;
}