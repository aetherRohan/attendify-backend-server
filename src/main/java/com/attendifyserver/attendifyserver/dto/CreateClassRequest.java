package com.attendifyserver.attendifyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClassRequest {
    private String className;
    private String section;
    private int duration;
}
