package com.vetclinic.api.service;

import com.vetclinic.api.dto.VisitDto;
import com.vetclinic.api.entity.*;
import com.vetclinic.api.exception.Ineligible;
import com.vetclinic.api.exception.NotFound;
import com.vetclinic.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vetclinic.api.utils.DateTimeUtil.getDayName;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
@Slf4j
public class VisitService {


    private final VisitRepository visitRepository;
    private final PetRepository petRepository;
    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final WorkingHoursRepository workingHoursRepo;

    public Visit createVisit(VisitDto dto) {
        log.debug("Attempting to create a new visit: {}", dto);

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new NotFound("Pet not found"));
        log.debug("Pet found: {}", pet);

        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new NotFound("Clinic not found"));
        log.debug("Clinic found: {}", clinic);

        Doctor doctor = doctorRepository.findByIdAndClinicId(dto.getDoctorId(), clinic.getId())
                .orElseThrow(() -> new NotFound("Doctor not found or not associated with given clinic"));
        log.debug("Doctor found: {}", doctor);

        Map<String, WorkingHours> shifts = new HashMap<>();
        for (WorkingHours wh: clinic.getWorkingHours())
            shifts.put(wh.getDay(), wh);
        log.debug("Clinic working hours parsed successfully, shifts found: {}", shifts.keySet());

        String visitDay = getDayName(dto.getDate().toLocalDate());
        LocalTime visitTime = dto.getDate().toLocalTime();
        log.debug("Attempt to validate visit day: {}, time: {}", visitDay, visitTime);

        if(!shifts.containsKey(visitDay)){
            log.debug("Visit date is out of clinic working days");
            throw new Ineligible("Visit date is out of clinic working days");
        }

        WorkingHours wh = shifts.get(visitDay);
        LocalTime startTime = wh.getStartTime();
        LocalTime endTime = wh.getEndTime();
        boolean withinShiftHours = !visitTime.isBefore(startTime) && !visitTime.isAfter(endTime);
        log.debug("Flag withinShiftHours set to {}", withinShiftHours);

        if (!withinShiftHours){
            String message = String.format("Visit time is out of clinic working hours from %s to %s",
                    startTime.toString(), endTime.toString());
            log.debug(message);
            throw new Ineligible(message);
        }

        log.debug("Validate doctor visits in {} at {}", visitDay, visitTime);
        List<Visit> visits = visitRepository.findByDoctorIdAndDate(doctor.getId(), dto.getDate());
        if (!visits.isEmpty()) {
            log.warn("Doctor: {} has a visit in {} at {}", doctor.getName(), visitDay, visitTime);
            throw new Ineligible("Provided doctor has a visit at the provided visit time");
        }
        log.debug("Doctor: {} has no visits in {} at {}", doctor.getName(), visitDay, visitTime);

        Visit visit = new Visit();
        visit.setPet(pet);
        visit.setDoctor(doctor);
        visit.setClinic(clinic);
        visit.setDate(dto.getDate());

        Visit savedVisit = visitRepository.save(visit);
        log.info("Visit saved successfully with id={}", savedVisit.getId());
        return savedVisit;
    }

    public Visit getVisitById(Long id) {
        log.debug("Fetching visit by id={}", id);
        return visitRepository.findById(id)
                .orElseThrow(() -> new NotFound("Visit not found"));
    }
}
