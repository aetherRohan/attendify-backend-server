package com.attendifyserver.attendifyserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(
        name = "class_session",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"class_id", "session_date"})
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSession {

    @Id
    @Column(name="id",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,updatable = false)
    private Date session_date;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "class_id")
    private Classes classes;

    @OneToMany(mappedBy = "classSessions",cascade = CascadeType.ALL)
    private List<Attendance> attendances;

}
