package com.healthcare.analytics.dashboard.repository;


import com.healthcare.analytics.dashboard.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByMedicalRecordNumber(String medicalRecordNumber);

    Page<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName, Pageable pageable);

    List<Patient> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    List<Patient> findByGender(Patient.Gender gender);

    @Query("SELECT p FROM Patient p WHERE p.id IN " +
           "(SELECT DISTINCT v.patient.id FROM Visit v WHERE v.visitDate >= :startDate)")
    List<Patient> findPatientsWithVisitsSince(@Param("startDate") LocalDate startDate);

    @Query("SELECT p FROM Patient p JOIN p.visits v GROUP BY p HAVING COUNT(v) >= :minVisits")
    List<Patient> findPatientsWithMinimumVisits(@Param("minVisits") Long minVisits);

    @Query("SELECT p FROM Patient p WHERE p.insuranceProvider = :insuranceProvider")
    List<Patient> findByInsuranceProvider(@Param("insuranceProvider") String insuranceProvider);

    @Query("SELECT COUNT(p) FROM Patient p")
    Long countTotalPatients();

    @Query("SELECT COUNT(DISTINCT p.id) FROM Patient p JOIN p.visits v " +
           "WHERE v.visitDate >= :startDate AND v.visitDate <= :endDate")
    Long countPatientsWithVisitsBetween(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}

