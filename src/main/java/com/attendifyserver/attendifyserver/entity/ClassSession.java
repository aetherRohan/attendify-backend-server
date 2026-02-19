package com.attendifyserver.attendifyserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "class_session")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date date;

    private LocalTime localTime;

    @ManyToOne()
    @JoinColumn(name = "class_id")
    private Classes classes;

    @OneToMany(mappedBy = "classSession")
    private List<Attendance> attendances;

}
