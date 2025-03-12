package com.vetclinic.api.service;

import com.vetclinic.api.adapter.PetAdapter;
import com.vetclinic.api.dto.PetDto;
import com.vetclinic.api.entity.Owner;
import com.vetclinic.api.entity.Pet;
import com.vetclinic.api.exception.NotFound;
import com.vetclinic.api.repository.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PetService {

    @Autowired private PetRepository petRepository;
    @Autowired private OwnerService ownerService;
    @Autowired private PetAdapter adapter;

    public ResponseEntity<?> createPet(PetDto petDto) {
        log.debug("Attempting to create pet in database, pet={}", petDto);

        long ownerId = petDto.getOwner().getId();

        // Fetch owner and handle validation
        Owner owner = ownerService.findById(ownerId).orElseThrow(() -> new NotFound("No owner found with provided id"));

        Pet pet = adapter.toEntity(petDto);
        pet.setOwner(owner);

        Pet savedPet;
        try {
            log.debug("Saving pet in database");
            savedPet = petRepository.save(pet);
            log.info("Pet created successfully with id={}", savedPet.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(adapter.toDto(savedPet));
        } catch (Exception e) {
            log.error("Error while saving pet in database, pet={}", petDto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating pet");
        }
    }

    public ResponseEntity<?> findPetById(Long id) {
        log.debug("Attempting to fetch pet with id={}", id);
        Pet pet = petRepository.findById(id).orElseThrow(() -> new  NotFound("No pet found with provided id"));
        log.debug("Pet found: {}", pet);
        return new ResponseEntity<>(adapter.toDto(pet), HttpStatus.CREATED);
    }

    public List<Pet> findAllPetWithOwnerId(Long id) {
        log.debug("Attempting to fetch pets with ownerId={}", id);
        List<Pet> pets = petRepository.findAllByOwnerId(id);
        log.debug("Pets found: {}", pets);
        return pets;
    }

}