package com.healthcare.analytics.dashboard.service;

import com.healthcare.analytics.dashboard.dto.CohortCriteriaDto;
import com.healthcare.analytics.dashboard.dto.CohortDto;
import com.healthcare.analytics.dashboard.dto.PatientAnalyticsDto;
import com.healthcare.analytics.dashboard.entity.CohortCriteria;
import com.healthcare.analytics.dashboard.entity.Patient;
import com.healthcare.analytics.dashboard.entity.PatientCohort;
import com.healthcare.analytics.dashboard.repository.PatientCohortRepository;
import com.healthcare.analytics.dashboard.repository.PatientRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientCohortService {

    @Autowired
    private PatientCohortRepository patientCohortRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PatientAnalyticsService patientAnalyticsService;


    public CohortDto createCohort(@Valid CohortDto cohortDto) {
        if(patientCohortRepository.existsByName(cohortDto.getName()))
            throw new EntityExistsException("Cohort with name '" + cohortDto.getName() + "' already exists");

        PatientCohort patientCohort = new PatientCohort();
        patientCohort.setName(cohortDto.getName());
        patientCohort.setDescription(cohortDto.getDescription());
        patientCohort.setCreatedBy(cohortDto.getCreatedBy());

        // Add criteria
        if (cohortDto.getCriteria() != null) {
            PatientCohort finalCohort = patientCohort;
            List<CohortCriteria> criteria = cohortDto.getCriteria().stream()
                    .map(dto -> mapCohortCriteriaDTOToCohortCriteria(dto, finalCohort))
                    .collect(Collectors.toList());
            patientCohort.setCriteria(criteria);
        }

        patientCohort = patientCohortRepository.save(patientCohort);

        // Apply criteria to find matching patients
        List<Patient> matchingPatients = findPatientsMatchingCriteria(patientCohort.getCriteria());
        patientCohort.setPatients(matchingPatients);
        patientCohort = patientCohortRepository.save(patientCohort);

        return mapPatientCohortToCohortDTO(patientCohort);
    }

    public List<CohortDto> getAllCohorts() {
        List<PatientCohort> cohorts = patientCohortRepository.findActiveCohortsOrderByUpdatedDesc();
        return cohorts.stream().map(this::mapPatientCohortToCohortDTO).collect(Collectors.toList());

    }

    public Page<CohortDto> searchCohorts(String searchTerm, Pageable pageable) {
        Page<PatientCohort> cohorts = patientCohortRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
        return cohorts.map(this::mapPatientCohortToCohortDTO);
    }

    public CohortDto getCohortById(Long cohortId) {
        Optional<PatientCohort> cohortOpt = patientCohortRepository.findById(cohortId);
        if (cohortOpt.isPresent()) {
            return mapPatientCohortToCohortDTO(cohortOpt.get());
        }
        throw new RuntimeException("Cohort not found with ID: " + cohortId);
    }

    public List<PatientAnalyticsDto> getCohortPatients(Long cohortId) {
        Optional<PatientCohort> cohortOpt = patientCohortRepository.findById(cohortId);
        if (cohortOpt.isPresent()) {
            return cohortOpt.get().getPatients().stream()
                    .map(patient -> patientAnalyticsService.getPatientAnalytics(patient.getId()))
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Cohort not found with ID: " + cohortId);
    }

    private List<Patient> findPatientsMatchingCriteria(List<CohortCriteria> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return patientRepository.findAll();
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT p FROM Patient p");
        StringBuilder whereClause = new StringBuilder(" WHERE ");
        List<Object> parameters = new ArrayList<>();
        int paramIndex = 1;

        for (int i = 0; i < criteria.size(); i++) {
            CohortCriteria criterion = criteria.get(i);

            if (i > 0) {
                whereClause.append(" ").append(criterion.getLogicalOperator().name()).append(" ");
            }

            String fieldName = criterion.getFieldName();
            String operator = criterion.getOperator().name();
            String value = criterion.getFieldValue();

            // Add joins for related entities if needed
            if (fieldName.startsWith("visit.") || fieldName.startsWith("diagnosis.")) {
                if (!queryBuilder.toString().contains("JOIN p.visits v")) {
                    queryBuilder.append(" LEFT JOIN p.visits v");
                }
                if (fieldName.startsWith("diagnosis.") && !queryBuilder.toString().contains("JOIN v.diagnoses d")) {
                    queryBuilder.append(" LEFT JOIN v.diagnoses d");
                }
            }

            // Build condition based on operator
            switch (CohortCriteria.OperatorType.valueOf(operator)) {
                case EQUALS:
                    whereClause.append("p.").append(fieldName).append(" = ?").append(paramIndex);
                    parameters.add(convertValue(fieldName, value));
                    break;
                case NOT_EQUALS:
                    whereClause.append("p.").append(fieldName).append(" != ?").append(paramIndex);
                    parameters.add(convertValue(fieldName, value));
                    break;
                case GREATER_THAN:
                    whereClause.append("p.").append(fieldName).append(" > ?").append(paramIndex);
                    parameters.add(convertValue(fieldName, value));
                    break;
                case LESS_THAN:
                    whereClause.append("p.").append(fieldName).append(" < ?").append(paramIndex);
                    parameters.add(convertValue(fieldName, value));
                    break;
                case CONTAINS:
                    whereClause.append("LOWER(p.").append(fieldName).append(") LIKE LOWER(?").append(paramIndex).append(")");
                    parameters.add("%" + value + "%");
                    break;
                case BETWEEN:
                    String[] values = value.split(",");
                    if (values.length == 2) {
                        whereClause.append("p.").append(fieldName).append(" BETWEEN ?").append(paramIndex)
                                .append(" AND ?").append(paramIndex + 1);
                        parameters.add(convertValue(fieldName, values[0].trim()));
                        parameters.add(convertValue(fieldName, values[1].trim()));
                        paramIndex++;
                    }
                    break;
            }
            paramIndex++;
        }

        String finalQuery = queryBuilder.toString() + whereClause.toString();
        Query query = entityManager.createQuery(finalQuery, Patient.class);

        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }

        return query.getResultList();
    }

    private Object convertValue(String fieldName, String value) {
        // Convert string values to appropriate types based on field name
        if (fieldName.equals("dateOfBirth")) {
            return LocalDate.parse(value);
        } else if (fieldName.equals("gender")) {
            return Patient.Gender.valueOf(value.toUpperCase());
        }
        // Add more conversions as needed
        return value;
    }

    private CohortDto mapPatientCohortToCohortDTO(PatientCohort cohort) {
        CohortDto cohortDto = new CohortDto();
        cohortDto.setId(cohort.getId());
        cohortDto.setName(cohort.getName());
        cohortDto.setDescription(cohort.getDescription());
        cohortDto.setCreatedBy(cohort.getCreatedBy());
        cohortDto.setCreatedAt(cohort.getCreatedAt());
        cohortDto.setUpdatedAt(cohort.getUpdatedAt());

        // Get patient count
        Long patientCount = patientCohortRepository.countPatientsByCohortId(cohort.getId());
        cohortDto.setPatientCount(patientCount);

        // Convert criteria
        if (cohort.getCriteria() != null) {
            List<CohortCriteriaDto> criteriaDto = cohort.getCriteria().stream()
                    .map(this::mapCohortCriteriaToCohortCriteriaDTO)
                    .collect(Collectors.toList());
            cohortDto.setCriteria(criteriaDto);
        }

        return cohortDto;
    }

    private CohortCriteriaDto mapCohortCriteriaToCohortCriteriaDTO(CohortCriteria criteria) {
        CohortCriteriaDto cohortCriteriaDto = new CohortCriteriaDto();
        cohortCriteriaDto.setId(criteria.getId());
        cohortCriteriaDto.setFieldName(criteria.getFieldName());
        cohortCriteriaDto.setOperator(criteria.getOperator().name());
        cohortCriteriaDto.setFieldValue(criteria.getFieldValue());
        cohortCriteriaDto.setLogicalOperator(criteria.getLogicalOperator().name());
        cohortCriteriaDto.setOrderIndex(criteria.getOrderIndex());

        return cohortCriteriaDto;
    }

    private CohortCriteria mapCohortCriteriaDTOToCohortCriteria(CohortCriteriaDto dto, PatientCohort cohort) {
        CohortCriteria cohortCriteria = new CohortCriteria();
        cohortCriteria.setCohort(cohort);
        cohortCriteria.setFieldName(dto.getFieldName());
        cohortCriteria.setOperator(CohortCriteria.OperatorType.valueOf(dto.getOperator()));
        cohortCriteria.setFieldValue(dto.getFieldValue());
        cohortCriteria.setLogicalOperator(CohortCriteria.LogicalOperator.valueOf(dto.getLogicalOperator()));
        cohortCriteria.setOrderIndex(dto.getOrderIndex());

        return cohortCriteria;
    }
}
