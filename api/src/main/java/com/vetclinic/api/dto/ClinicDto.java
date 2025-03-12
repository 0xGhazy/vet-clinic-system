package com.vetclinic.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vetclinic.api.entity.Doctor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^01[0-25]\\d{8}$", message = "Invalid phone number format")
    private String phone;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Valid
    @NotNull(message = "Working hours is required")
    @NotEmpty(message = "Working hours is required")
    private List<WorkingHoursDto> workingHours;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> socialNetworks;

    @JsonInclude(JsonInclude.Include.NON_NULL)
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
                ", doctors=" + doctors +
                '}';
    }
}
