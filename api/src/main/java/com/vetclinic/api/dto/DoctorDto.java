package com.vetclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoctorDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^01[0-25]\\d{8}$", message = "Invalid phone number format")
    private String phone;

    private String photo;

    @Size(max = 200, message = "Bio must not exceed 200 characters")
    private String bio;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
