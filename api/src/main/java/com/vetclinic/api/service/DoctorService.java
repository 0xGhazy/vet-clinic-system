package com.vetclinic.api.service;

import com.vetclinic.api.adapter.DoctorAdapter;
import com.vetclinic.api.entity.Doctor;
import com.vetclinic.api.exception.NotFound;
import com.vetclinic.api.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;

@Slf4j
@Service
public class DoctorService {


    @Autowired
    private DoctorRepository repository;
    @Autowired
    private DoctorAdapter doctorAdapter;


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
