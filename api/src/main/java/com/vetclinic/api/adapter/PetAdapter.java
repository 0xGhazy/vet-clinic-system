package com.vetclinic.api.adapter;

import com.vetclinic.api.dto.PetDto;
import com.vetclinic.api.entity.Pet;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PetAdapter {

    private final ModelMapper mapper;

    public PetDto toDto(Pet entity) {
        return mapper.map(entity, PetDto.class);
    }

    public Pet toEntity(PetDto dto) {
        return mapper.map(dto, Pet.class);
    }

    public List<PetDto> toDtoList(List<Pet> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

}
