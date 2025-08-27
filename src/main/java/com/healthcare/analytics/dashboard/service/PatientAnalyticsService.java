package com.healthcare.analytics.dashboard.service;

import com.healthcare.analytics.dashboard.dto.DiagnosisDto;
import com.healthcare.analytics.dashboard.dto.PatientAnalyticsDto;
import com.healthcare.analytics.dashboard.entity.Diagnosis;
import com.healthcare.analytics.dashboard.entity.Patient;
import com.healthcare.analytics.dashboard.entity.Visit;
import com.healthcare.analytics.dashboard.repository.DiagnosisRepository;
import com.healthcare.analytics.dashboard.repository.PatientRepository;
import com.healthcare.analytics.dashboard.repository.VisitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientAnalyticsService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private DiagnosisRepository diagnosisRepository;

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
        throw new EntityNotFoundException("Patient with ID " +patientId+ " is not found.");
    }

    /**
     * @description Returns patient analytics for provided search term.
     **/
    public Page<PatientAnalyticsDto> searchPatients(String searchTerm, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                searchTerm, searchTerm, pageable);
        return patients.map(this::mapPatientToPatientAnalyticsDTO);    }

    public PatientAnalyticsDto getPatientAnalytics(Long patientId) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isPresent()) {
            return mapPatientToPatientAnalyticsDTO(patientOpt.get());
        }
        throw new RuntimeException("Patient not found with ID: " + patientId);
    }

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

        // Calculate age
        if (patient.getDateOfBirth() != null) {
            patientAnalyticsDto.setAge(Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears());
        }

        // Analytics data
        Long totalVisits = visitRepository.countVisitsByPatientId(patient.getId());
        patientAnalyticsDto.setTotalVisits(totalVisits);

        // Visits in the last year
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        Long visitsLastYear = visitRepository.countVisitsByPatientIdAndDateRange(
                patient.getId(), oneYearAgo, LocalDateTime.now());
        patientAnalyticsDto.setVisitsLastYear(visitsLastYear);

        // Get recent visits to find last visit date and primary doctor
        List<Visit> recentVisits = visitRepository.findRecentVisitsByPatientId(
                patient.getId(), LocalDateTime.now().minusYears(5));

        if (!recentVisits.isEmpty()) {
            patientAnalyticsDto.setLastVisitDate(recentVisits.get(0).getVisitDate());

            // Find most frequent doctor
            String primaryDoctor = recentVisits.stream()
                    .collect(Collectors.groupingBy(
                            v -> v.getDoctor().getFirstName() + " " + v.getDoctor().getLastName(),
                            Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
            patientAnalyticsDto.setPrimaryDoctor(primaryDoctor);
        }

        // Recent diagnoses (last 5)
        List<Diagnosis> diagnoses = diagnosisRepository.findByPatientId(patient.getId());
        List<DiagnosisDto> recentDiagnoses = diagnoses.stream()
                .limit(5)
                .map(this::mapDiagnosisToDiagnosisDTO)
                .collect(Collectors.toList());
        patientAnalyticsDto.setRecentDiagnoses(recentDiagnoses);

        return patientAnalyticsDto;
    }

    private DiagnosisDto mapDiagnosisToDiagnosisDTO(Diagnosis diagnosis) {
        DiagnosisDto diagnosisDto = new DiagnosisDto();
        diagnosisDto.setId(diagnosis.getId());
        diagnosisDto.setDiagnosisCode(diagnosis.getDiagnosisCode());
        diagnosisDto.setDiagnosisName(diagnosis.getDiagnosisName());
        diagnosisDto.setDescription(diagnosis.getDescription());
        diagnosisDto.setDiagnosisType(diagnosis.getDiagnosisType().name());
        diagnosisDto.setSeverity(diagnosis.getSeverity().name());
        diagnosisDto.setCreatedAt(diagnosis.getCreatedAt());
        return diagnosisDto;
    }
}
