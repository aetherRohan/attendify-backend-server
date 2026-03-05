package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.dto.ClassResponse;
import com.attendifyserver.attendifyserver.dto.StudentResponse;
import com.attendifyserver.attendifyserver.entity.Classes;
import com.attendifyserver.attendifyserver.repository.ClassRepository;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;



}
