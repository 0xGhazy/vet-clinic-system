package com.vetclinic.api.adapter;

import com.vetclinic.api.dto.ClinicDto;
import com.vetclinic.api.entity.Clinic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClinicAdapter {

    @Autowired
    private ModelMapper mapper;

    public ClinicDto toDto(Clinic clinic) {
        return mapper.map(clinic, ClinicDto.class);
    }

    public Clinic toEntity(ClinicDto clinicDto) {
        return mapper.map(clinicDto, Clinic.class);
    }

    public List<ClinicDto> toDtoList(List<Clinic> clinics) {
        return clinics.stream().map(this::toDto).collect(Collectors.toList());
    }
}
