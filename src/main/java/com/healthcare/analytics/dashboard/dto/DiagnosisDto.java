package com.healthcare.analytics.dashboard.dto;

import java.time.LocalDateTime;

public class DiagnosisDto {

    private Long id;
    private String diagnosisCode;
    private String diagnosisName;
    private String description;
    private String diagnosisType;
    private String severity;
    private LocalDateTime createdAt;

    // Constructors
    public DiagnosisDto() {}

    public DiagnosisDto(String diagnosisCode, String diagnosisName, String diagnosisType, String severity) {
        this.diagnosisCode = diagnosisCode;
        this.diagnosisName = diagnosisName;
        this.diagnosisType = diagnosisType;
        this.severity = severity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDiagnosisCode() { return diagnosisCode; }
    public void setDiagnosisCode(String diagnosisCode) { this.diagnosisCode = diagnosisCode; }

    public String getDiagnosisName() { return diagnosisName; }
    public void setDiagnosisName(String diagnosisName) { this.diagnosisName = diagnosisName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDiagnosisType() { return diagnosisType; }
    public void setDiagnosisType(String diagnosisType) { this.diagnosisType = diagnosisType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
