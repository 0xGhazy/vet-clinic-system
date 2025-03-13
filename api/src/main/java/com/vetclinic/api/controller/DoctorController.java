package com.vetclinic.api.controller;

import com.vetclinic.api.adapter.DoctorAdapter;
import com.vetclinic.api.dto.DoctorDto;
import com.vetclinic.api.entity.Doctor;
import com.vetclinic.api.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorAdapter adapter;

    @PostMapping
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorDto dto) {
        log.debug("Attempting to save doctor in database, doctor={}", dto);
        ResponseEntity<?> result = doctorService.createDoctor(dto);
        log.info("Doctor saved successfully");
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        log.debug("Fetching doctor by id={}", id);
        Doctor doctor = doctorService.findDoctorById(id);
        log.info("Doctor found, id={}, doctor={}", id, doctor);
        return new ResponseEntity<>(adapter.toDto(doctor), HttpStatus.OK);
    }
}
