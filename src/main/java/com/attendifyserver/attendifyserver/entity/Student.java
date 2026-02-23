package com.attendifyserver.attendifyserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "students")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BleUuid",unique = true,updatable = false,nullable = false)
    private UUID bleUuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Attendance> attendances;

    @ManyToMany(mappedBy = "students")
    private List<Classes>classes;

    private String rollNumber;
    private String semester;
    private String department;

    @PrePersist
    private void generateBLEUuid(){
        if (this.bleUuid ==null){
            this.bleUuid = UUID.randomUUID();
        }
    }

}
