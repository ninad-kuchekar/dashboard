package com.healthcare.analytics.dashboard.repository;

import com.healthcare.analytics.dashboard.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByVisitId(Long visitId);

    List<Prescription> findByMedicationNameContainingIgnoreCase(String medicationName);

    List<Prescription> findByStatus(Prescription.PrescriptionStatus status);

    @Query("SELECT p FROM Prescription p WHERE p.visit.patient.id = :patientId ORDER BY p.createdAt DESC")
    List<Prescription> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT p.medicationName, COUNT(p) FROM Prescription p " +
           "GROUP BY p.medicationName ORDER BY COUNT(p) DESC")
    List<Object[]> getMostPrescribedMedications();

    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.visit.patient.id = :patientId")
    Long countPrescriptionsByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.visit.patient.id = :patientId AND p.status = :status")
    Long countPrescriptionsByPatientIdAndStatus(@Param("patientId") Long patientId,
                                               @Param("status") Prescription.PrescriptionStatus status);
}

