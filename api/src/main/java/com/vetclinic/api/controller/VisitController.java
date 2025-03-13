package com.vetclinic.api.controller;

import com.vetclinic.api.adapter.VisitAdapter;
import com.vetclinic.api.dto.VisitDto;
import com.vetclinic.api.entity.Visit;
import com.vetclinic.api.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitAdapter adapter;

    @PostMapping
    public ResponseEntity<?> createVisit(@Valid @RequestBody VisitDto dto) {
        log.debug("Attempting to create visit, visitDto={}", dto);
        Visit visit = visitService.createVisit(dto);
        log.info("Visit created successfully with id={}", visit.getId());
        return ResponseEntity.status(201).body(adapter.toDto(visit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitById(@PathVariable Long id) {
        log.debug("Fetching visit by id={}", id);
        Visit visit = visitService.getVisitById(id);
        return ResponseEntity.ok(adapter.toDto(visit));
    }
}