package com.attendifyserver.attendifyserver.controller;


import com.attendifyserver.attendifyserver.dto.*;
import com.attendifyserver.attendifyserver.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;


    @PostMapping("/signup/student")
    public ResponseEntity<?> registerStudent(@RequestBody SignupRequest request) {
        try {
            LoginResponse response = authService.registerStudent(request);
            return ResponseEntity.ok(response);

        } catch (ResponseStatusException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(new MessageResponse(exception.getReason()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }


    @PostMapping("/signup/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody SignupRequest request) {
        try {
            LoginResponse response = authService.registerTeacher(request);
            return ResponseEntity.ok(response);

        } catch (ResponseStatusException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(new MessageResponse(exception.getReason()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Something Went Wrong"));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Wrong Id or Password"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshTokenRequest refreshToken) {
        try {
            TokenResponse response = authService.createRefreshToken(refreshToken);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }



}
