package com.attendifyserver.attendifyserver.config;

import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import com.attendifyserver.attendifyserver.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {


    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Student> studentOPT = this.studentRepository.findByEmail(username);
        if (studentOPT.isPresent()) {
            return new CustomUserDetails(studentOPT.get());
        }
        Optional<Teacher> teacherOPT = this.teacherRepository.findByEmail(username);
        if (teacherOPT.isPresent()) {
            return new CustomUserDetails(teacherOPT.get());
        }
        throw new UsernameNotFoundException("User not found!");
    }
}
