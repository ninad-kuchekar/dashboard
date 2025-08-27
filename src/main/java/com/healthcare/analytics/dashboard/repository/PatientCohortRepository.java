package com.healthcare.analytics.dashboard.repository;

import com.healthcare.analytics.dashboard.entity.PatientCohort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientCohortRepository extends JpaRepository<PatientCohort, Long> {

    List<PatientCohort> findByCreatedBy(String createdBy);

    List<PatientCohort> findByIsActiveTrue();

    Page<PatientCohort> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT pc FROM PatientCohort pc WHERE pc.isActive = true ORDER BY pc.updatedAt DESC")
    List<PatientCohort> findActiveCohortsOrderByUpdatedDesc();

    @Query("SELECT COUNT(p) FROM PatientCohort pc JOIN pc.patients p WHERE pc.id = :cohortId")
    Long countPatientsByCohortId(@Param("cohortId") Long cohortId);

    boolean existsByName(String name);
}

