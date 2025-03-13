package com.vetclinic.api.repository;

import com.vetclinic.api.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByDoctorIdAndDate(Long doctorId, LocalDateTime date);
}
