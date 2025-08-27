package com.healthcare.analytics.dashboard.controller;

import com.healthcare.analytics.dashboard.dto.CohortDto;
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
    private PatientCohortService cohortService;

    @PostMapping
    public ResponseEntity<CohortDto> createCohort(@Valid @RequestBody CohortDto cohortDto) {
        CohortDto createdCohort = cohortService.createCohort(cohortDto);
        return new ResponseEntity<>(createdCohort, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CohortDto>> getAllCohorts() {
        List<CohortDto> cohorts = cohortService.getAllCohorts();
        return ResponseEntity.ok(cohorts);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CohortDto>> searchCohorts(
            @RequestParam String q,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CohortDto> cohorts = cohortService.searchCohorts(q, pageable);
        return ResponseEntity.ok(cohorts);
    }
}
