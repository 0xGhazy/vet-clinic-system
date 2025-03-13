package com.vetclinic.api.adapter;

import com.vetclinic.api.dto.OwnerDto;
import com.vetclinic.api.entity.Owner;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerAdapter {
    private final ModelMapper mapper;

    public OwnerDto toDto(Owner entity) {
        return mapper.map(entity, OwnerDto.class);
    }

    public Owner toEntity(OwnerDto dto) {
        return mapper.map(dto, Owner.class);
    }

}
