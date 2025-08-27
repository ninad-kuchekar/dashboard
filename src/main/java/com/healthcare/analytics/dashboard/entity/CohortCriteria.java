package com.healthcare.analytics.dashboard.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cohort_criteria", indexes = {
    @Index(name = "idx_criteria_cohort", columnList = "cohort_id"),
    @Index(name = "idx_criteria_field", columnList = "fieldName")
})
@EntityListeners(AuditingEntityListener.class)
public class CohortCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cohort_id", nullable = false)
    @NotNull(message = "Cohort is required")
    private PatientCohort cohort;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Field name is required")
    private String fieldName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperatorType operator;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Field value is required")
    private String fieldValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogicalOperator logicalOperator = LogicalOperator.AND;

    @Column(nullable = false)
    private Integer orderIndex = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public CohortCriteria() {}

    public CohortCriteria(PatientCohort cohort, String fieldName, OperatorType operator, String fieldValue) {
        this.cohort = cohort;
        this.fieldName = fieldName;
        this.operator = operator;
        this.fieldValue = fieldValue;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PatientCohort getCohort() { return cohort; }
    public void setCohort(PatientCohort cohort) { this.cohort = cohort; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public OperatorType getOperator() { return operator; }
    public void setOperator(OperatorType operator) { this.operator = operator; }

    public String getFieldValue() { return fieldValue; }
    public void setFieldValue(String fieldValue) { this.fieldValue = fieldValue; }

    public LogicalOperator getLogicalOperator() { return logicalOperator; }
    public void setLogicalOperator(LogicalOperator logicalOperator) { this.logicalOperator = logicalOperator; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum OperatorType {
        EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL, CONTAINS, NOT_CONTAINS, IN, NOT_IN, BETWEEN, IS_NULL, IS_NOT_NULL
    }

    public enum LogicalOperator {
        AND, OR
    }
}

