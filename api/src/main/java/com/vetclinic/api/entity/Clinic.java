package com.vetclinic.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clinics",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_clinic_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_clinic_phone", columnNames = "phone")
        })
@Data
@NoArgsConstructor
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkingHours> workingHours = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "clinic_social_links", joinColumns = @JoinColumn(name = "clinic_id"))
    @Column(name = "social_network_url")
    private List<String> socialNetworks;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Doctor> doctors;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", workingHours=" + workingHours +
                ", socialNetworks=" + socialNetworks +
                '}';
    }
}
