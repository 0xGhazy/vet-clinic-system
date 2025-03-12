package com.vetclinic.api.service;

import com.vetclinic.api.adapter.DoctorAdapter;
import com.vetclinic.api.dto.DoctorDto;
import com.vetclinic.api.entity.Doctor;
import com.vetclinic.api.exception.BadRequest;
import com.vetclinic.api.exception.InternalServerError;
import com.vetclinic.api.exception.NotFound;
import com.vetclinic.api.repository.DoctorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository repository;
    @Autowired
    private DoctorAdapter doctorAdapter;

    public ResponseEntity<?> createDoctor(DoctorDto dto) {
        log.debug("Mapping Doctor DTO to entity, dto={}", dto);
        Doctor doctor = doctorAdapter.toEntity(dto);
        log.debug("Doctor DTO to entity, doctor={}", doctor);
        Doctor savedDoctor;
        try {
            savedDoctor = repository.save(doctor);
            log.info("Doctor saved successfully with id={}", savedDoctor.getId());
            return new ResponseEntity<>(doctorAdapter.toDto(savedDoctor), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Error while creating doctor in db");
            if (ex.getMessage().contains("uk_doctor_phone")) {
                log.warn("Provided doctor phone is already exists");
                throw new BadRequest("Doctor phone is already exist");
            } else {
                log.error("Error while creating doctor in db, ex={}", ex.getMessage());
                throw new InternalServerError("Unexpected error occur");
            }
        }
    }

    public Doctor findDoctorById(long id) {
        log.debug("Try to fetch doctor by id={}", id);
        Doctor doctor = repository.findById(id).orElseThrow(() -> new NotFound("No doctor found with provided id"));
        log.debug("Doctor loaded successfully, doctor={}", doctor);
        return doctor;
    }

    public Doctor updateDoctor(Doctor newDoctor) {
        log.debug("Try to update doctor with id={}", newDoctor.getId());
        Doctor doctor = repository.save(newDoctor);
        log.debug("Doctor updated successfully");
        return doctor;
    }
}
