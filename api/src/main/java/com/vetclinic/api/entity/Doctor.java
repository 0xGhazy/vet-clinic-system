package com.vetclinic.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid phone number format")
    @Column(unique = true)
    private String phone;

    private String photo; // URL or base64 encoded string

    @Size(max = 200, message = "Bio must not exceed 200 characters")
    private String bio;

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = true) // allow setting null at unassign operation
    @JsonIgnore
    @ToString.Exclude
    private Clinic clinic;
}
