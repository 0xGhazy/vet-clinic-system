package com.vetclinic.api.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitDto {
    private Long id;
    @NotNull(message = "Pet is required")
    private Long petId;
    @NotNull(message = "Doctor is required")
    private Long doctorId;
    @NotNull(message = "Clinic is required")
    private Long clinicId;

    @NotNull(message = "Visit date is required")
    @FutureOrPresent(message = "Visit date must be in the present or future")
    private LocalDateTime date;
}
