package com.vetclinic.api.service;

import com.vetclinic.api.adapter.ClinicAdapter;
import com.vetclinic.api.adapter.DoctorAdapter;
import com.vetclinic.api.adapter.WorkingHoursAdapter;
import com.vetclinic.api.dto.ClinicDto;
import com.vetclinic.api.entity.Clinic;
import com.vetclinic.api.entity.Doctor;
import com.vetclinic.api.entity.WorkingHours;
import com.vetclinic.api.enums.ClinicDoctorOperation;
import com.vetclinic.api.exception.BadRequest;
import com.vetclinic.api.exception.Ineligible;
import com.vetclinic.api.exception.InternalServerError;
import com.vetclinic.api.exception.NotFound;
import com.vetclinic.api.repository.ClinicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClinicService {

    @Autowired private ClinicAdapter clinicAdapter;
    @Autowired private DoctorAdapter doctorAdapter;
    @Autowired private DoctorService doctorService;
    @Autowired private ClinicRepository repository;
    @Autowired private WorkingHoursAdapter workingHoursAdapter;

    public ResponseEntity<?> createClinic(ClinicDto clinicDto) {
        log.debug("Create new clinic={}", clinicDto);

        Clinic entity = clinicAdapter.toEntity(clinicDto);
        log.debug("ClinicDto mapped to Clinic entity={}", entity);

        // Set working hours to be inserted with the clinic
        if (clinicDto.getWorkingHours() != null) {
            List<WorkingHours> workingHoursList = clinicDto.getWorkingHours().stream()
                    .map(dto -> {
                        WorkingHours workingHours = workingHoursAdapter.toEntity(dto);
                        workingHours.setClinic(entity);
                        return workingHours;
                    }).collect(Collectors.toList());

            entity.setWorkingHours(workingHoursList);
        }
        log.info("Working hours set successfully, clinic={}", entity);


        log.debug("Start presenting clinic to db");
        Clinic clinic;
        try {
            clinic = repository.save(entity);
            log.info("Clinic stored in db successfully, created clinic id={}", clinic.getId());
            return new ResponseEntity<>(clinicAdapter.toDto(clinic), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Error while creating clinic in db");
            if (ex.getMessage().contains("uk_clinic_email")) {
                log.warn("Provided clinic email is already exists");
                throw new BadRequest("Clinic email is already exist");
            } else if (ex.getMessage().contains("uk_clinic_phone")) {
                log.warn("Provided clinic phone is already exists");
                throw new BadRequest("Clinic phone is already exist");
            }
            log.error("Error while creating clinic in db, ex={}", ex.getMessage());
            throw new InternalServerError("Unexpected error occur");
        }
    }

    public ResponseEntity<?> findClinicById(long id) {
        log.debug("Try to fetch clinic by id={}", id);

        Clinic clinic = repository.findById(id).orElseThrow( () -> new NotFound("No clinic is found with provided id"));
        log.info("Clinic fetched successfully, clinic={}", clinic);
        return new ResponseEntity<>(clinic, HttpStatus.OK);

    }

    public ResponseEntity<?> search(String phone, String address) {
        log.debug("Search for clinic with address or phone started");
        boolean paramsIsNull = (phone == null) && address == null;
        if (paramsIsNull) {
            log.warn("Two parameters are null value, fetching all clinics will be invoked");
            return fetchAll();
        }
        log.debug("Start searching for clinics with phone contains [{}], address contains [{}]", phone, address);
        List<Clinic> clinics = repository.findByPhoneIgnoreCaseContainingOrAddressIgnoreCaseContaining(phone, address);
        log.info("[{}] Clinic are matching criteria=[phone like %{}% or address like %{}%]",
                clinics.size(),
                phone,
                address);
        return new ResponseEntity<>(clinicAdapter.toDtoList(clinics), HttpStatus.OK);
    }

    private ResponseEntity<?> fetchAll() {
        log.debug("Fetching all clinics started");
        List<Clinic> clinics = repository.findAll();
        if (clinics.isEmpty()) {
            log.warn("No clinics are found in database");
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
        log.info("[{}] Clinic are loaded successfully", clinics.size());
        return new ResponseEntity<>(clinicAdapter.toDtoList(clinics), HttpStatus.OK);
    }

    public ResponseEntity<?> fetchClinicDoctorsByClinicId(long id) {
        log.debug("Fetching clinic doctors with clinic id={}", id);
        Clinic clinic = repository.findById(id).orElseThrow( () -> new NotFound("No clinic is found with provided id"));
        log.debug("Clinic is loaded successfully, clinic={}", clinic);

        log.debug("Try to extract doctors list from clinic entity");
        if (clinic.getDoctors().isEmpty()) {
            log.debug("No doctors are found in this clinic");
            return ResponseEntity.ok(List.of());
        }
        List<Doctor> doctors = clinic.getDoctors();
        log.debug("[{}] Doctors are associated with this clinic", doctors.size());
        return ResponseEntity.ok(doctorAdapter.toDtoList(doctors));
    }

    public ResponseEntity<?> clinicDoctorAssignment(long clinicId, long doctorId, String operation) {

        boolean doctorInClinicStaff = false;
        String doctorName, clinicName;

        // Validate operation parameter before do any database interaction.
        log.debug("Try to validate operation parameter, operation={}", operation);
        if (!isValid(operation)) {
            log.debug("Invalid value for operation, expect=[ASSIGN, REMOVE], but got {}", operation);
            throw new BadRequest("Invalid operation parameter value. Allowed: ASSIGN, REMOVE");
        }

        // Validate clinic exists in the database
        log.debug("Try to validate if clinic exists in database");
        Clinic clinic = repository.findById(clinicId).orElseThrow(() -> new NotFound("No clinic is found with provided id"));
        clinicName = clinic.getName();
        log.debug("Clinic exists in database, clinic={}", clinic);

        // Fetch doctor to check if the doctor exists in the database.
        log.debug("Try to validate if doctor exists in database");
        Doctor doctor = doctorService.findDoctorById(doctorId);
        doctorName = doctor.getName();
        log.debug("Doctor exists in database, doctor={}", doctor);

        // If the user tries to assign this doctor again to a new clinic the getClinic() -> null;
        boolean reassignedDoctor = (doctor.getClinic() == null);
        log.info("Try to reassign doctor {} to clinic {}", doctorName, clinicName);

        // Validate if the doctor is already assigned to this clinic
        log.debug("Try to validate if doctor is already in the clinic staff");
        if (!reassignedDoctor && doctor.getClinic().getId() == clinicId){
            doctorInClinicStaff = true;
            log.debug("Doctor {} is already assigned to {} clinic", doctorName, clinicName);
            log.debug("doctorInClinicStaff flag set to {}", doctorInClinicStaff);
        }

        if (ClinicDoctorOperation.ASSIGN.name().equals(operation.toUpperCase())) {
            log.debug("Start assigning doctor {} to {} clinic", doctorName, clinicName);
            // doctor can't be assigned again
            if (doctorInClinicStaff) {
                throw new Ineligible("Doctor is already assigned to this clinic");
            }

            Doctor assignedDoctor = doctor;
            assignedDoctor.setClinic(clinic);
            log.debug("Try to update doctor {} clinic", doctorName);
            log.debug("Old doctor record={}, new doctor record={}", doctor, assignedDoctor);
            assignedDoctor = doctorService.updateDoctor(assignedDoctor);

            if (Objects.equals(assignedDoctor.getClinic().getId(), clinic.getId())){
                log.debug("Doctor {} assigned to {} clinic successfully", doctorName, clinicName);
                return ResponseEntity.ok("{\"message\":\"doctor assigned successfully\"}");
            }
            log.error("Failed to update doctor record, oldRecord={}. new record={}, clinic={}", doctor, assignedDoctor, clinic);
            throw new InternalServerError("Failed to assign provided doctorId to the provided clinicId");
        }

        log.debug("Start unassigning doctor {} from {} clinic", doctorName, clinicName);
        if (!doctorInClinicStaff) {
            throw new Ineligible("Doctor isn't assigned to this clinic");
        }

        Doctor assignedDoctor = doctor;
        assignedDoctor.setClinic(null);
        log.debug("Try to update doctor {} clinic", doctorName);
        log.debug("Old doctor record={}, new doctor record={}", doctor, assignedDoctor);
        assignedDoctor = doctorService.updateDoctor(assignedDoctor);
        log.info("Update result={}", assignedDoctor);
        return ResponseEntity.ok("{\"message\":\"doctor removed from clinic successfully\"}");
    }

    private boolean isValid(String op) {
        try {
            ClinicDoctorOperation.valueOf(op.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
