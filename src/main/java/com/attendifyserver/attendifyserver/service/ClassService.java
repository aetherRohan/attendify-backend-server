package com.attendifyserver.attendifyserver.service;

import com.attendifyserver.attendifyserver.dto.*;
import com.attendifyserver.attendifyserver.entity.Classes;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import com.attendifyserver.attendifyserver.repository.ClassRepository;
import com.attendifyserver.attendifyserver.repository.StudentRepository;
import com.attendifyserver.attendifyserver.repository.TeacherRepository;
import com.attendifyserver.attendifyserver.util.ClassCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClassService {


    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    @Transactional
    public ClassResponse createClass(CreateClassRequest classRequest) {

        String teacherEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Teacher loggedInTeacher = teacherRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Teacher not  found"
                ));

        boolean classExists = classRepository.existsByTeacherAndClassNameAndSection(
                loggedInTeacher,
                classRequest.getClassName(),
                classRequest.getSection()
        );
        if (classExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "You have already created a class with this name and section."
            );
        }

        String codeGenerated;
        do {
            codeGenerated = ClassCodeGenerator.generate();
        } while (classRepository.existsByClassCode(codeGenerated));

        Classes newClass = Classes.builder()
                .className(classRequest.getClassName())
                .classDuration(classRequest.getDuration())
                .section(classRequest.getSection())
                .classCode(codeGenerated)
                .teacher(loggedInTeacher)
                .build();

        Classes savedClass = classRepository.save(newClass);

        return ClassResponse.builder()
                .classId(savedClass.getId())
                .classCode(savedClass.getClassCode())
                .className(savedClass.getClassName())
                .section(savedClass.getSection())
                .duration(savedClass.getClassDuration())
                .message("Class created successfully")
                .build();
    }

    public ClassResponse joinClass(String classCode) {

        String studentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Student> studentOptional = studentRepository.findByEmail(studentEmail);
        Optional<Classes> classOptional = classRepository.findByClassCode(classCode);

        if (studentOptional.isPresent() && classOptional.isPresent()) {

            if (classRepository.existsByStudentsAndClassCode(studentOptional.get(), classCode)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You are already enrolled");
            } else {
                Classes classes = classOptional.get();
                classes.getStudents().add(studentOptional.get());

                Classes savedClass = classRepository.save(classes);

                return ClassResponse.builder()
                        .classId(savedClass.getId())
                        .classCode(savedClass.getClassCode())
                        .className(savedClass.getClassName())
                        .message("Enroll Success")
                        .build();
            }
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong Class Code");
    }


    public List<ClassResponse> getStudentClasses() {

        List<Classes> rawClasses;

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Student> optStudent = studentRepository.findByEmail(email);

        if (optStudent.isPresent()) {
            Student student=optStudent.get();

            rawClasses = classRepository.findByStudentsId(student.getId());
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not found");

        return rawClasses.stream().map(
                classes -> ClassResponse.builder()
                        .className(classes.getClassName())
                        .classCode(classes.getClassCode())
                        .section(classes.getSection())
                        .classId(classes.getId())
                        .duration(classes.getClassDuration())
                        .message("")
                        .build()
        ).collect(Collectors.toList());
    }

    public List<ClassResponse> getTeacherClasses() {

        List<Classes> rawClasses;

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Teacher> optionalTeacher = teacherRepository.findByEmail(email);

        if (optionalTeacher.isPresent()) {
            Teacher teacher= optionalTeacher.get();

            rawClasses = classRepository.findByTeacherId(teacher.getId());
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Not found");

        return rawClasses.stream().map(
                classes -> ClassResponse.builder()
                        .className(classes.getClassName())
                        .classCode(classes.getClassCode())
                        .section(classes.getSection())
                        .classId(classes.getId())
                        .duration(classes.getClassDuration())
                        .message("")
                        .build()
        ).collect(Collectors.toList());
    }


    public List<StudentResponse> getStudentsForClass(Long classId) {

        if(!classRepository.existsById(classId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Class doesn't exist");
        }

        List<Student> rawStudentList = classRepository.findStudentsByClassId(classId);

        return rawStudentList.stream().map(
                student -> StudentResponse.builder()
                        .name(student.getName())
                        .studentId(student.getId())
                        .classId(classId)
                        .rollNumber(student.getRollNumber())
                        .bleUuid(student.getBleUuid().toString())
                        .build()
        ).collect(Collectors.toList());
    }
}
