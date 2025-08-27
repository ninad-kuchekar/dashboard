package com.healthcare.analytics.dashboard.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PatientAnalyticsDto {

    private Long id;
    private String medicalRecordNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String insuranceProvider;

    // Analytics fields
    private Long totalVisits;
    private Long visitsLastYear;
    private LocalDateTime lastVisitDate;
    private List<DiagnosisDto> recentDiagnoses;
    private String primaryDoctor;
    private Integer age;

    // Constructors
    public PatientAnalyticsDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMedicalRecordNumber() { return medicalRecordNumber; }
    public void setMedicalRecordNumber(String medicalRecordNumber) { this.medicalRecordNumber = medicalRecordNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }

    public Long getTotalVisits() { return totalVisits; }
    public void setTotalVisits(Long totalVisits) { this.totalVisits = totalVisits; }

    public Long getVisitsLastYear() { return visitsLastYear; }
    public void setVisitsLastYear(Long visitsLastYear) { this.visitsLastYear = visitsLastYear; }

    public LocalDateTime getLastVisitDate() { return lastVisitDate; }
    public void setLastVisitDate(LocalDateTime lastVisitDate) { this.lastVisitDate = lastVisitDate; }

    public List<DiagnosisDto> getRecentDiagnoses() { return recentDiagnoses; }
    public void setRecentDiagnoses(List<DiagnosisDto> recentDiagnoses) { this.recentDiagnoses = recentDiagnoses; }

    public String getPrimaryDoctor() { return primaryDoctor; }
    public void setPrimaryDoctor(String primaryDoctor) { this.primaryDoctor = primaryDoctor; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
