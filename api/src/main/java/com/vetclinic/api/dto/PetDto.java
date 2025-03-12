package com.vetclinic.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vetclinic.api.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class PetDto {
    private Long id;

    @NotBlank(message = "Pet name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Animal kind is required")
    private String animalKind;

    private List<String> photos = List.of();

    @NotNull(message = "Weight is required")
    private Double weight;

    @Valid
    @NotNull(message = "Owner id is required")
    private PetOwnerDto owner;

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", dateOfBirth=" + dateOfBirth +
                ", animalKind='" + animalKind + '\'' +
                ", photos=" + photos +
                ", weight=" + weight +
                ", owner=" + owner +
                '}';
    }
}
