package com.healthcare.analytics.dashboard.controller;

import com.healthcare.analytics.dashboard.dto.PatientAnalyticsDto;
import com.healthcare.analytics.dashboard.service.PatientAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
public class PatientAnalyticsController {

    @Autowired
    private PatientAnalyticsService patientAnalyticsService;

    @GetMapping
    public ResponseEntity<Page<PatientAnalyticsDto>> getAllPatients(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PatientAnalyticsDto> patients = patientAnalyticsService.getAllPatientsWithAnalytics(pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientAnalyticsDto> getPatientById(@PathVariable Long patientId) {
        PatientAnalyticsDto patient = patientAnalyticsService.getPatientAnalytics(patientId);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PatientAnalyticsDto>> searchPatients(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PatientAnalyticsDto> patients = patientAnalyticsService.searchPatients(q, pageable);
        return ResponseEntity.ok(patients);
    }
}
