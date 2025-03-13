package com.vetclinic.api.repository;

import com.vetclinic.api.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByPhoneIgnoreCaseContainingOrAddressIgnoreCaseContaining(String phone, String address);
}
