package com.attendifyserver.attendifyserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassSessionResponse {
    private long classSessionId;
    private long classId;
    private String date;
}
