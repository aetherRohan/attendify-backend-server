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
        name = "class_session",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"class_id", "sessionDate"})
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
    private String sessionDate;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "class_id")
    private Classes classes;

    @OneToMany(mappedBy = "classSessions",cascade = CascadeType.ALL)
    private List<Attendance> attendances;

}
