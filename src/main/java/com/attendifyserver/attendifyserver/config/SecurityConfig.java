package com.attendifyserver.attendifyserver.config;

import com.attendifyserver.attendifyserver.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

//    private final JwtAuthenticationEntryPoint jwtEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
//                .exceptionHandling(exception -> exception
////                        .authenticationEntryPoint(jwtEntryPoint)
//                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/student/**").hasAuthority(Roles.STUDENT.name())
                        .requestMatchers("/api/teacher/**").hasAuthority(Roles.TEACHER.name())
                        .requestMatchers("/api/auth/signup/student",
                                "/api/auth/signup/teacher",
                                "/api/auth/login","/api/auth/refreshToken").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}