package com.attendifyserver.attendifyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
public class SyncOfflineSessionResponse {

    private String status;
    private Long sessionId;
    private Long classId;
    private Map<String, Integer> studentAttendance;
}
