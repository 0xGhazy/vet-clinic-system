package com.vetclinic.api.adapter;

import com.vetclinic.api.dto.VisitDto;
import com.vetclinic.api.entity.Visit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitAdapter {

    private final ModelMapper mapper;

    public VisitDto toDto(Visit entity) {
        VisitDto dto = new VisitDto();
        dto.setId(entity.getId());
        dto.setClinicId(entity.getClinic().getId());
        dto.setDoctorId(entity.getDoctor().getId());
        dto.setDate(entity.getDate());
        dto.setPetId(entity.getPet().getId());
        return dto;
    }
}
