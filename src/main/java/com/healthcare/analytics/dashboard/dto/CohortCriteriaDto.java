package com.healthcare.analytics.dashboard.dto;
public class CohortCriteriaDto {

    private Long id;
    private String fieldName;
    private String operator;
    private String fieldValue;
    private String logicalOperator;
    private Integer orderIndex;

    // Constructors
    public CohortCriteriaDto() {}

    public CohortCriteriaDto(String fieldName, String operator, String fieldValue) {
        this.fieldName = fieldName;
        this.operator = operator;
        this.fieldValue = fieldValue;
        this.logicalOperator = "AND";
        this.orderIndex = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getFieldValue() { return fieldValue; }
    public void setFieldValue(String fieldValue) { this.fieldValue = fieldValue; }

    public String getLogicalOperator() { return logicalOperator; }
    public void setLogicalOperator(String logicalOperator) { this.logicalOperator = logicalOperator; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}

