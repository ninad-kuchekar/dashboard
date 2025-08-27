package com.healthcare.analytics.dashboard.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patient_cohorts", indexes = {
    @Index(name = "idx_cohort_name", columnList = "name"),
    @Index(name = "idx_cohort_created_by", columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
public class PatientCohort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Cohort name is required")
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Created by is required")
    private String createdBy;

    @OneToMany(mappedBy = "cohort", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CohortCriteria> criteria;

    @ManyToMany
    @JoinTable(
        name = "cohort_patients",
        joinColumns = @JoinColumn(name = "cohort_id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id"),
        indexes = {
            @Index(name = "idx_cohort_patients_cohort", columnList = "cohort_id"),
            @Index(name = "idx_cohort_patients_patient", columnList = "patient_id")
        }
    )
    private List<Patient> patients;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public PatientCohort() {}

    public PatientCohort(String name, String description, String createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<CohortCriteria> getCriteria() { return criteria; }
    public void setCriteria(List<CohortCriteria> criteria) { this.criteria = criteria; }

    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

