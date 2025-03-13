package com.vetclinic.api.controller;

import com.vetclinic.api.dto.ClinicDto;
import com.vetclinic.api.service.ClinicService;
import com.vetclinic.api.utils.IDGenerator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/clinics")
public class ClinicController {

    @Autowired ClinicService service;

    @PostMapping
    public ResponseEntity<?> create (@Valid @RequestBody ClinicDto clinicDto) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Create clinic invoked, clinicDto={}", clinicDto);
        ResponseEntity<?> result = service.createClinic(clinicDto);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Create clinic finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

    @GetMapping("/{clinicId}")
    public ResponseEntity<?> retrieveClinicById (@PathVariable long clinicId) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Fetching clinic by id={} started", clinicId);
        ResponseEntity<?> result = service.findClinicById(clinicId);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Fetching clinic finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

    @GetMapping
    public ResponseEntity<?> search (@RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "address", required = false) String address) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Clinic search started, parameters=[phone={}, address={}]", phone, address);
        ResponseEntity<?> result = service.search(phone, address);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Clinic search finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

    @GetMapping("/{clinicId}/doctors")
    public ResponseEntity<?> fetchClinicDoctors (@PathVariable long clinicId) {
        log.info("Fetching clinic doctors started, parameters=[clinicId={}]", clinicId);
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        ResponseEntity<?> result = service.fetchClinicDoctorsByClinicId(clinicId);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Fetching clinic doctors finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

    @PostMapping(value = "/{clinicId}/doctors/{doctorId}/{operation}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> clinicDoctorAssignment(
            @PathVariable long clinicId,
            @PathVariable long doctorId,
            @PathVariable String operation) {
        MDC.put("trxId", IDGenerator.trxId());
        long startTime = System.currentTimeMillis();
        log.info("Clinic doctor assignment started, parameters=[clinicId={}, doctorId={}, operation={}]",
                clinicId,
                doctorId,
                operation);

        ResponseEntity<?> result = service.clinicDoctorAssignment(clinicId, doctorId, operation);
        long duration = System.currentTimeMillis() - startTime;
        log.info("Clinic doctor assignment finished successfully, statusCode={}, response={}, duration={}Ms",
                result.getStatusCode(),
                result.getBody(),
                duration);
        MDC.clear();
        return result;
    }

}
