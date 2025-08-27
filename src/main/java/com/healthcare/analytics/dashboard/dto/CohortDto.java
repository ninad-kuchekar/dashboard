package com.healthcare.analytics.dashboard.dto;
import java.time.LocalDateTime;
import java.util.List;

public class CohortDto {

    private Long id;
    private String name;
    private String description;
    private String createdBy;
    private Long patientCount;
    private List<CohortCriteriaDto> criteria;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CohortDto() {}

    public CohortDto(String name, String description, String createdBy) {
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

    public Long getPatientCount() { return patientCount; }
    public void setPatientCount(Long patientCount) { this.patientCount = patientCount; }

    public List<CohortCriteriaDto> getCriteria() { return criteria; }
    public void setCriteria(List<CohortCriteriaDto> criteria) { this.criteria = criteria; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

