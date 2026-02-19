package com.attendifyserver.attendifyserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "classSession_id"})
        }
)

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isPresent;
;
    @ManyToOne()
    @JoinColumn(name = "student_id",nullable = false)
    private Student student;

    @ManyToOne()
    @JoinColumn(name = "classSession_id",nullable = false)
    private ClassSession classSession;

}
