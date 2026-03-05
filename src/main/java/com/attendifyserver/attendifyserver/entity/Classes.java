package com.attendifyserver.attendifyserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@Table(
        name = "class",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"teacher_id", "class_name","section"})
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 7)
    private String classCode;

    @Column(nullable = false)
    private String className;

    @Column(name = "duration_Minutes",nullable = false)
    private int classDuration;

    private String section;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "teacher_id",nullable = false)
    private Teacher teacher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "class_student",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    @OneToMany(mappedBy = "classes",cascade = CascadeType.ALL)
    private List<ClassSession> classSessions;

}
