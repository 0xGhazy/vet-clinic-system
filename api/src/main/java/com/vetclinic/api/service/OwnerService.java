package com.vetclinic.api.service;

import com.vetclinic.api.adapter.OwnerAdapter;
import com.vetclinic.api.adapter.PetAdapter;
import com.vetclinic.api.dto.OwnerDto;
import com.vetclinic.api.entity.Owner;
import com.vetclinic.api.entity.Pet;
import com.vetclinic.api.exception.BadRequest;
import com.vetclinic.api.exception.InternalServerError;
import com.vetclinic.api.exception.NotFound;
import com.vetclinic.api.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerAdapter ownerAdapter;
    private final PetAdapter petAdapter;

    public ResponseEntity<?> retrieveOwnerById(long id) {
        log.debug("Attempting to fetch owner by id={}", id);
        Owner owner = findById(id).orElseThrow(() -> new NotFound("No owner found with provided id"));
        log.info("Owner found: {}", owner);
        return ResponseEntity.status(HttpStatus.OK).body(ownerAdapter.toDto(owner));
    }

    public ResponseEntity<?> createOwner(OwnerDto dto) {
        log.debug("Attempting to save owner in database, owner={}", dto);

        // Create pets entity
        Owner entity = ownerAdapter.toEntity(dto);
        log.debug("Dto mapped to entity successfully, entity={}", entity);

        if (dto.getPets() != null) {
            List<Pet> pets = dto.getPets().stream()
                    .map(petDto -> {
                        Pet pet = petAdapter.toEntity(petDto);
                        pet.setOwner(entity);
                        return pet;
                    }).collect(Collectors.toList());
            entity.setPets(pets);
        }
        log.info("Pets set successfully, owner={}", entity);

        Owner owner;
        try {
            owner = save(entity);
            log.debug("Owner saved successfully with id={}", owner.getId());
        } catch (Exception ex) {
            log.error("Error while creating clinic in db, dto={}, entity={}, ex={}", dto, entity, ex);
            if (ex.getMessage().contains("uk_owner_email")) {
                log.warn("Provided owner email is already exists");
                throw new BadRequest("Owner email is already exist");
            } else if (ex.getMessage().contains("uk_owner_phone")) {
                log.warn("Provided Owner phone is already exists");
                throw new BadRequest("Owner phone is already exist");
            }
            throw new InternalServerError("Unexpected error occur");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerAdapter.toDto(owner));
    }


    public ResponseEntity<?> findOwnerPets(long id) {
        log.debug("Attempting to fetch owner's pets with ownerId={}", id);
        Owner owner = findById(id).orElseThrow(() -> new NotFound("No owner found with provided id"));
        List<Pet> pets = owner.getPets();
        log.info("[{}] Pets found", pets);
        return ResponseEntity.status(HttpStatus.OK).body(petAdapter.toDtoList(pets));
    }

    public Optional<Owner> findById(Long id) {
        log.debug("Trying to fetch owner with id={}", id);
        return ownerRepository.findById(id);
    }

    private Owner save(Owner owner) {
        log.debug("Trying to save owner={}", owner);
        return ownerRepository.save(owner);
    }
}