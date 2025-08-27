package com.healthcare.analytics.dashboard.repository;

import com.healthcare.analytics.dashboard.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByDepartment(String department);

    Page<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName, Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization AND d.department = :department")
    List<Doctor> findBySpecializationAndDepartment(@Param("specialization") String specialization,
                                                  @Param("department") String department);

    @Query("SELECT COUNT(v) FROM Visit v WHERE v.doctor.id = :doctorId")
    Long countPatientsByDoctorId(@Param("doctorId") Long doctorId);
}

