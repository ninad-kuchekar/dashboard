package com.healthcare.analytics.dashboard.repository;

import com.healthcare.analytics.dashboard.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByPatientIdOrderByVisitDateDesc(Long patientId);

    List<Visit> findByDoctorIdOrderByVisitDateDesc(Long doctorId);

    Page<Visit> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<Visit> findByVisitType(Visit.VisitType visitType);

    List<Visit> findByStatus(Visit.VisitStatus status);

    @Query("SELECT COUNT(v) FROM Visit v WHERE v.patient.id = :patientId")
    Long countVisitsByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT COUNT(v) FROM Visit v WHERE v.patient.id = :patientId " +
           "AND v.visitDate >= :startDate AND v.visitDate <= :endDate")
    Long countVisitsByPatientIdAndDateRange(@Param("patientId") Long patientId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId " +
           "AND v.visitDate >= :startDate ORDER BY v.visitDate DESC")
    List<Visit> findRecentVisitsByPatientId(@Param("patientId") Long patientId,
                                          @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(v) FROM Visit v WHERE v.doctor.id = :doctorId")
    Long countVisitsByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT v.visitType, COUNT(v) FROM Visit v GROUP BY v.visitType")
    List<Object[]> getVisitTypeStatistics();
}
