package com.attendifyserver.attendifyserver.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String message;
    private String role;
    private Long userId;
    private String fullName;

    //ADD THIS FIELD FOR JWT LATER
    private String token;

    public LoginResponse(String message, String role, Long userId, String fullName, String token) {
        this.message = message;
        this.role = role;
        this.userId = userId;
        this.fullName = fullName;
        this.token = token;
    }
}
