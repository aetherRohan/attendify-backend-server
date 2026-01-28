package com.attendifyserver.attendifyserver.repository;

import com.attendifyserver.attendifyserver.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student>  findByEmail( String email);
     Boolean existsByEmail(String email);

}
