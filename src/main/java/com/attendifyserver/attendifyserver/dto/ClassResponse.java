package com.attendifyserver.attendifyserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassResponse {

    private Long classId;
    private String classCode;
    private String className;
    private String section;
    private String message;
    private int duration;

}
