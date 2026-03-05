package com.attendifyserver.attendifyserver.repository;

import com.attendifyserver.attendifyserver.entity.Classes;
import com.attendifyserver.attendifyserver.entity.Student;
import com.attendifyserver.attendifyserver.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ClassRepository extends JpaRepository<Classes, Long> {

    boolean existsByClassCode(String classCode);

    boolean existsByTeacherAndClassNameAndSection(Teacher teacher, String className, String section);

    boolean existsByStudentsAndClassCode(Student students, String classCode);

    Optional<Classes> findByClassCode(String classCode);

    @Query("select s from Classes c JOIN c.students s where c.id=:classId")
    List<Student> findStudentsByClassId(@Param("classId") Long classId);

    List<Classes> findByStudentsId(Long studentId);
}
