package com.vetclinic.api.controller;

import com.vetclinic.api.dto.OwnerDto;
import com.vetclinic.api.service.OwnerService;
import com.vetclinic.api.utils.IDGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<?> createOwner(@Valid @RequestBody OwnerDto dto) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Creating owner request started, parameters=[owner={}]", dto);

        ResponseEntity<?> result = ownerService.createOwner(dto);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Creating owner finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveOwnerById(@PathVariable Long id) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Fetching owner request started, parameters=[id={}]", id);
        ResponseEntity<?> result = ownerService.retrieveOwnerById(id);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Fetching owner finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

    @GetMapping("/{id}/pets")
    public ResponseEntity<?> retrieveOwnerPets(@PathVariable Long id) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Fetching owner pets request started, parameters=[id={}]", id);
        ResponseEntity<?> result = ownerService.findOwnerPets(id);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Fetching owner pets finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

}

