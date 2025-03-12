package com.vetclinic.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class WorkingHoursDto {
    @Pattern(regexp = "(?i)^(SUNDAY|MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY)$",
            message = "Invalid day name. Must be a valid weekday.")
    @NotBlank(message = "Day is required")
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public String toString() {
        return "{" +
                "day='" + day + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

