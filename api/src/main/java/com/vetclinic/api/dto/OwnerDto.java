package com.vetclinic.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vetclinic.api.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OwnerDto {
    private Long id;
    @NotBlank(message = "Owner name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^01[0-25]\\d{8}$", message = "Invalid phone number format")
    private String phone;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Address is required")
    private String address;

    @Valid
    @NotNull(message = "Pets is required")
    @JsonIgnore
    private List<PetDto> pets = List.of();

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", pets=" + pets +
                '}';
    }
}
