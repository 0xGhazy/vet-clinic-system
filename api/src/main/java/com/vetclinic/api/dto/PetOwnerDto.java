package com.vetclinic.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PetOwnerDto {
    @NotNull(message = "Owner id is requires")
    @Min(value = 0, message = "Owner id can't be negative value")
    private long id;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                '}';
    }
}
