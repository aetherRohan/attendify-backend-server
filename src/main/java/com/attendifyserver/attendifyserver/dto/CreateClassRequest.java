package com.attendifyserver.attendifyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClassRequest {
    private String className;
    private String section;
    private String duration;
}
