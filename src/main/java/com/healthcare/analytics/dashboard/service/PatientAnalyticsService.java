package com.healthcare.analytics.dashboard.service;

import com.healthcare.analytics.dashboard.dto.PatientAnalyticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PatientAnalyticsService {


    public Page<PatientAnalyticsDto> getAllPatientsWithAnalytics(Pageable pageable) {
        return null;
    }

    public PatientAnalyticsDto getPatientAnalytics(Long patientId) {
        return null;
    }

    public Page<PatientAnalyticsDto> searchPatients(String q, Pageable pageable) {
        return null;
    }
}
