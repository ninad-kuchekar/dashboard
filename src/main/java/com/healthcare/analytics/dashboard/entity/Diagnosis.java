package com.healthcare.analytics.dashboard.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagnoses", indexes = {
    @Index(name = "idx_diagnosis_visit", columnList = "visit_id"),
    @Index(name = "idx_diagnosis_code", columnList = "diagnosisCode"),
    @Index(name = "idx_diagnosis_name", columnList = "diagnosisName")
})
@EntityListeners(AuditingEntityListener.class)
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    @NotNull(message = "Visit is required")
    private Visit visit;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Diagnosis code is required")
    private String diagnosisCode;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Diagnosis name is required")
    private String diagnosisName;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiagnosisType diagnosisType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Diagnosis() {}

    public Diagnosis(Visit visit, String diagnosisCode, String diagnosisName, DiagnosisType diagnosisType, Severity severity) {
        this.visit = visit;
        this.diagnosisCode = diagnosisCode;
        this.diagnosisName = diagnosisName;
        this.diagnosisType = diagnosisType;
        this.severity = severity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }

    public String getDiagnosisCode() { return diagnosisCode; }
    public void setDiagnosisCode(String diagnosisCode) { this.diagnosisCode = diagnosisCode; }

    public String getDiagnosisName() { return diagnosisName; }
    public void setDiagnosisName(String diagnosisName) { this.diagnosisName = diagnosisName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DiagnosisType getDiagnosisType() { return diagnosisType; }
    public void setDiagnosisType(DiagnosisType diagnosisType) { this.diagnosisType = diagnosisType; }

    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum DiagnosisType {
        PRIMARY, SECONDARY, CHRONIC, ACUTE, DIFFERENTIAL
    }

    public enum Severity {
        MILD, MODERATE, SEVERE, CRITICAL
    }
}

