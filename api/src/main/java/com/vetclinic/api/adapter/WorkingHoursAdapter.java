package com.vetclinic.api.adapter;

import com.vetclinic.api.dto.DoctorDto;
import com.vetclinic.api.dto.WorkingHoursDto;
import com.vetclinic.api.entity.Doctor;
import com.vetclinic.api.entity.WorkingHours;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorkingHoursAdapter {

    @Autowired
    private ModelMapper mapper;

    public WorkingHoursDto toDto(WorkingHours entity) {
        return mapper.map(entity, WorkingHoursDto.class);
    }

    public WorkingHours toEntity(WorkingHoursDto dto) {
        return mapper.map(dto, WorkingHours.class);
    }

    public List<WorkingHoursDto> toDtoList(List<WorkingHours> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

}
