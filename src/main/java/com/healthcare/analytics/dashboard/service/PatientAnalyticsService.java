package com.healthcare.analytics.dashboard.service;

import com.healthcare.analytics.dashboard.dto.PatientAnalyticsDto;
import com.healthcare.analytics.dashboard.entity.Patient;
import com.healthcare.analytics.dashboard.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class PatientAnalyticsService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * @description Returns all patient analytics
     **/
    public Page<PatientAnalyticsDto> getAllPatientsWithAnalytics(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(this::mapPatientToPatientAnalyticsDTO);
    }

    /**
     * @description Returns patient analytics for provided patientId
     **/
    public PatientAnalyticsDto getPatientAnalyticsByID(Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        if(patient.isPresent())
            return mapPatientToPatientAnalyticsDTO(patient.get());
        throw new RuntimeException("Patient with ID " +patientId+ " is not found.");
    }

    /**
     * @description Returns patient analytics for provided search term.
     **/
    public Page<PatientAnalyticsDto> searchPatients(String searchTerm, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm, searchTerm, pageable);
        return patients.map(this::mapPatientToPatientAnalyticsDTO);    }


    /**
     * @description Mapping from entity to DTO for Patient.
     **/
    private PatientAnalyticsDto mapPatientToPatientAnalyticsDTO(Patient patient) {
        PatientAnalyticsDto patientAnalyticsDto = new PatientAnalyticsDto();

        // Basic patient information
        patientAnalyticsDto.setId(patient.getId());
        patientAnalyticsDto.setMedicalRecordNumber(patient.getMedicalRecordNumber());
        patientAnalyticsDto.setFirstName(patient.getFirstName());
        patientAnalyticsDto.setLastName(patient.getLastName());
        patientAnalyticsDto.setDateOfBirth(patient.getDateOfBirth());
        patientAnalyticsDto.setGender(patient.getGender().name());
        patientAnalyticsDto.setPhoneNumber(patient.getPhoneNumber());
        patientAnalyticsDto.setEmail(patient.getEmail());
        patientAnalyticsDto.setInsuranceProvider(patient.getInsuranceProvider());



        return patientAnalyticsDto;
    }
}
