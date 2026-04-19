package com.attendifyserver.attendifyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncOfflineSessionRequest {
    private Long classId;
    private String sessionDate;
    private Integer totalWindowsCount;
    private Map<UUID, Integer> studentWindowCounts;
}