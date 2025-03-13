package com.vetclinic.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vetclinic.api.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String animalKind;

    @ElementCollection
    @CollectionTable(name = "pet_photos", joinColumns = @JoinColumn(name = "pet_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    private Double weight;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", dateOfBirth=" + dateOfBirth +
                ", animalKind='" + animalKind + '\'' +
                ", photos=" + photos +
                ", weight=" + weight +
                ", owner=" + owner +
                '}';
    }
}

