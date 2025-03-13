package com.vetclinic.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "doctors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_doctor_phone", columnNames = "phone")
        })
@Data
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String photo;
    private String bio;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = true)
    @JsonIgnore
    @ToString.Exclude
    private Clinic clinic;
}
