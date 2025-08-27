package com.healthcare.analytics.dashboard.repository;

import com.healthcare.analytics.dashboard.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    List<Diagnosis> findByVisitId(Long visitId);

    List<Diagnosis> findByDiagnosisCode(String diagnosisCode);

    List<Diagnosis> findByDiagnosisNameContainingIgnoreCase(String diagnosisName);

    List<Diagnosis> findByDiagnosisType(Diagnosis.DiagnosisType diagnosisType);

    List<Diagnosis> findBySeverity(Diagnosis.Severity severity);

    @Query("SELECT d FROM Diagnosis d WHERE d.visit.patient.id = :patientId ORDER BY d.createdAt DESC")
    List<Diagnosis> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT d.diagnosisCode, d.diagnosisName, COUNT(d) FROM Diagnosis d " +
           "GROUP BY d.diagnosisCode, d.diagnosisName ORDER BY COUNT(d) DESC")
    List<Object[]> getMostCommonDiagnoses();

    @Query("SELECT COUNT(DISTINCT d.diagnosisCode) FROM Diagnosis d WHERE d.visit.patient.id = :patientId")
    Long countUniqueDiagnosesByPatientId(@Param("patientId") Long patientId);
}

