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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date session_date;

    private LocalTime localTime;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "class_id")
    private Classes classes;

    @OneToMany(mappedBy = "classSessions",cascade = CascadeType.ALL)
    private List<Attendance> attendances;

}
