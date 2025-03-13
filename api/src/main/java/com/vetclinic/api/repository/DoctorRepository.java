package com.vetclinic.api.repository;

import com.vetclinic.api.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByIdAndClinicId(Long doctorId, Long clinicId);
}
