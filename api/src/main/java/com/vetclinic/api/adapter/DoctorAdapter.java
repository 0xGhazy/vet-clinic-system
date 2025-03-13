package com.vetclinic.api.adapter;

import com.vetclinic.api.dto.DoctorDto;
import com.vetclinic.api.entity.Doctor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DoctorAdapter {
    private final ModelMapper mapper;

    public DoctorDto toDto(Doctor entity) {
        return mapper.map(entity, DoctorDto.class);
    }

    public Doctor toEntity(DoctorDto dto) {
        return mapper.map(dto, Doctor.class);
    }

    public List<DoctorDto> toDtoList(List<Doctor> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

}
