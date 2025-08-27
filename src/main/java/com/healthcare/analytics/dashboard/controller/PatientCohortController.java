package com.healthcare.analytics.dashboard.controller;

import com.healthcare.analytics.dashboard.dto.CohortDto;
import com.healthcare.analytics.dashboard.dto.PatientAnalyticsDto;
import com.healthcare.analytics.dashboard.service.PatientCohortService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cohorts")
public class PatientCohortController {

    @Autowired
    private PatientCohortService patientCohortService;

    @PostMapping
    public ResponseEntity<CohortDto> createCohort(@Valid @RequestBody CohortDto cohortDto) {
        CohortDto createdCohort = patientCohortService.createCohort(cohortDto);
        return new ResponseEntity<>(createdCohort, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CohortDto>> getAllCohorts() {
        List<CohortDto> cohorts = patientCohortService.getAllCohorts();
        return ResponseEntity.ok(cohorts);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CohortDto>> searchCohorts(
            @RequestParam String q,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CohortDto> cohorts = patientCohortService.searchCohorts(q, pageable);
        return ResponseEntity.ok(cohorts);
    }

    @GetMapping("/{cohortId}")
    public ResponseEntity<CohortDto> getCohortById(@PathVariable Long cohortId) {
        CohortDto cohort = patientCohortService.getCohortById(cohortId);
        return ResponseEntity.ok(cohort);
    }

    @GetMapping("/{cohortId}/patients")
    public ResponseEntity<List<PatientAnalyticsDto>> getCohortPatients(@PathVariable Long cohortId) {
        List<PatientAnalyticsDto> patients = patientCohortService.getCohortPatients(cohortId);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{cohortId}")
    public ResponseEntity<CohortDto> updateCohort(
            @PathVariable Long cohortId,
            @Valid @RequestBody CohortDto cohortDto) {
        CohortDto updatedCohort = patientCohortService.updateCohort(cohortId, cohortDto);
        return ResponseEntity.ok(updatedCohort);
    }

    @DeleteMapping("/{cohortId}")
    public ResponseEntity<Void> deleteCohort(@PathVariable Long cohortId) {
        patientCohortService.deleteCohort(cohortId);
        return ResponseEntity.noContent().build();
    }
}
