package com.attendifyserver.attendifyserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginResponse {
    private String role;
    private Long userId;
    private String name;
    private String token;
}
