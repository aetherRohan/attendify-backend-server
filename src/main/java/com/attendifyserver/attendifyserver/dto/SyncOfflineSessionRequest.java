package com.attendifyserver.attendifyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncOfflineSessionRequest {

    private Long classId;
    private LocalDate sessionDate;
    private Integer durationMinutes;
    private Integer totalWindowsCount;
    private Map<String, Integer> studentWindowCounts;

}