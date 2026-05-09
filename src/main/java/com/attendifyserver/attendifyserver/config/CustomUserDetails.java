package com.attendifyserver.attendifyserver.config;


import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final String authorities;
    private final long userId;
    private final String name;
    private  UUID bleUuid;

    public CustomUserDetails(Student student) {
        this.email = student.getEmail();
        this.password = student.getPassword();
        this.authorities = "STUDENT";
        this.userId = student.getId();
        this.name = student.getName();
        this.bleUuid=student.getBleUuid();
    }

    public CustomUserDetails(Teacher teacher) {
        this.email = teacher.getEmail();
        this.password = teacher.getPassword();
        this.authorities = "TEACHER";
        this.userId = teacher.getId();
        this.name = teacher.getName();
    }


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(authorities));
    }

    @Override
    public  String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
