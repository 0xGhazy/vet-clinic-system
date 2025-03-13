package com.vetclinic.api.controller;

import com.vetclinic.api.dto.PetDto;
import com.vetclinic.api.service.PetService;
import com.vetclinic.api.utils.IDGenerator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/pets")
public class PetController {

    @Autowired private PetService service;

    @PostMapping
    public ResponseEntity<?> createPet(@Valid @RequestBody PetDto petDto) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Create pet invoked, pet={}", petDto);
        ResponseEntity<?> result = service.createPet(petDto);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Create pet finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrievePetById(@PathVariable Long id) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Fetching pet by id={} started", id);
        ResponseEntity<?> result = service.findPetById(id);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Fetching pet finished successfully, statusCode={}, response={}, duration={}Ms",
                    result.getStatusCode(),
                    result.getBody(),
                    duration);
            MDC.clear();
            return result;
    }
}
