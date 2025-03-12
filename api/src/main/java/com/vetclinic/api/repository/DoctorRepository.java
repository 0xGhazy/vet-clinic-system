package com.vetclinic.api.repository;

import com.vetclinic.api.entity.Clinic;
import com.vetclinic.api.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Modifying
    @Query("UPDATE Doctor d SET d.clinic = :clinic WHERE d.id = :doctorId")
    void assignDoctorToClinic(@Param("doctorId") Long doctorId, @Param("clinic") Clinic clinic);

    @Modifying
    @Query("UPDATE Doctor d SET d.clinic = NULL WHERE d.id = :doctorId AND d.clinic.id = :clinicId")
    void unassignDoctorFromClinic(@Param("doctorId") Long doctorId, @Param("clinicId") Long clinicId);
}

