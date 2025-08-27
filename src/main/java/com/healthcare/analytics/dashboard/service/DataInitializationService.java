package com.healthcare.analytics.dashboard.service;

import com.healthcare.analytics.dashboard.repository.*;
import com.healthcare.analytics.dashboard.repository.*;
import com.healthcare.analytics.dashboard.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (patientRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create sample doctors
        List<Doctor> doctors = createSampleDoctors();
        doctorRepository.saveAll(doctors);

        // Create sample patients
        List<Patient> patients = createSamplePatients();
        patientRepository.saveAll(patients);

        // Create sample visits with diagnoses and prescriptions
        createSampleVisitsWithData(patients, doctors);
    }

    private List<Doctor> createSampleDoctors() {
        return Arrays.asList(
                new Doctor("John", "Smith", "MD001", "Cardiology",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("Sarah", "Johnson", "MD002", "Pediatrics",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("Michael", "Brown", "MD003", "Orthopedics",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("Emily", "Davis", "MD004", "Dermatology",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("Robert", "Wilson", "MD005", "Internal Medicine",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("Lisa", "Anderson", "MD006", "Neurology",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("David", "Taylor", "MD007", "Emergency Medicine",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("Jennifer", "Thomas", "MD008", "Oncology",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("James", "Jackson", "MD009", "Psychiatry",LocalDateTime.now(),LocalDateTime.now()),
                new Doctor("Amanda", "White", "MD010", "Radiology",LocalDateTime.now(),LocalDateTime.now())
        );
    }

    private List<Patient> createSamplePatients() {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Hannah",
                "Ian", "Julia", "Kevin", "Laura", "Mark", "Nancy", "Oscar", "Patricia"};
        String[] lastNames = {"Anderson", "Brown", "Clark", "Davis", "Evans", "Foster", "Garcia", "Harris",
                "Johnson", "King", "Lewis", "Miller", "Nelson", "Parker", "Quinn", "Roberts"};
        String[] insuranceProviders = {"Blue Cross", "Aetna", "Cigna", "UnitedHealth", "Humana"};

        List<Patient> patients = new java.util.ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            Patient patient = new Patient();
            patient.setMedicalRecordNumber(String.format("MRN%06d", i));
            patient.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            patient.setLastName(lastNames[random.nextInt(lastNames.length)]);
            patient.setDateOfBirth(LocalDate.now().minusYears(random.nextInt(80) + 1).minusDays(random.nextInt(365)));
            patient.setGender(Patient.Gender.values()[random.nextInt(Patient.Gender.values().length)]);
            patient.setPhoneNumber(String.format("555-%03d-%04d", random.nextInt(1000), random.nextInt(10000)));
            patient.setEmail(patient.getFirstName().toLowerCase() + "." + patient.getLastName().toLowerCase() + "@email.com");
            patient.setAddress(String.format("%d Main St, City %d", random.nextInt(9999) + 1, random.nextInt(100) + 1));
            patient.setInsuranceProvider(insuranceProviders[random.nextInt(insuranceProviders.length)]);
            patient.setInsurancePolicyNumber(String.format("POL%08d", random.nextInt(100000000)));
            patient.setCreatedAt(LocalDateTime.now());
            patient.setUpdatedAt(LocalDateTime.now());
            patients.add(patient);
        }

        return patients;
    }

    private void createSampleVisitsWithData(List<Patient> patients, List<Doctor> doctors) {
        String[] diagnosisCodes = {"I10", "E11.9", "M79.3", "J45.9", "K21.9", "F32.9", "M25.50", "H52.4", "L70.0", "N39.0"};
        String[] diagnosisNames = {"Essential Hypertension", "Type 2 Diabetes", "Fibromyalgia", "Asthma",
                "GERD", "Major Depression", "Arthritis", "Presbyopia", "Acne", "UTI"};
        String[] medications = {"Lisinopril", "Metformin", "Albuterol", "Omeprazole", "Sertraline",
                "Ibuprofen", "Atorvastatin", "Amlodipine", "Levothyroxine", "Amoxicillin"};

        for (Patient patient : patients) {
            // Create 1-10 visits per patient
            int numVisits = random.nextInt(10) + 1;

            for (int v = 0; v < numVisits; v++) {
                Visit visit = new Visit();
                visit.setPatient(patient);
                visit.setDoctor(doctors.get(random.nextInt(doctors.size())));
                visit.setVisitDate(LocalDateTime.now().minusDays(random.nextInt(730))); // Last 2 years
                visit.setVisitType(Visit.VisitType.values()[random.nextInt(Visit.VisitType.values().length)]);
                visit.setStatus(Visit.VisitStatus.COMPLETED);
                visit.setReasonForVisit("Regular checkup and treatment");
                visit.setNotes("Patient examined, vitals stable");
                visit.setTotalCost(BigDecimal.valueOf(random.nextDouble() * 500 + 50));
                visit.setCreatedAt(LocalDateTime.now());
                visit.setUpdatedAt(LocalDateTime.now());

                visit = visitRepository.save(visit);

                // Add 1-3 diagnoses per visit
                int numDiagnoses = random.nextInt(3) + 1;
                for (int d = 0; d < numDiagnoses; d++) {
                    int diagIndex = random.nextInt(diagnosisCodes.length);
                    Diagnosis diagnosis = new Diagnosis();
                    diagnosis.setVisit(visit);
                    diagnosis.setDiagnosisCode(diagnosisCodes[diagIndex]);
                    diagnosis.setDiagnosisName(diagnosisNames[diagIndex]);
                    diagnosis.setDescription("Clinical diagnosis based on examination");
                    diagnosis.setDiagnosisType(Diagnosis.DiagnosisType.values()[random.nextInt(Diagnosis.DiagnosisType.values().length)]);
                    diagnosis.setSeverity(Diagnosis.Severity.values()[random.nextInt(Diagnosis.Severity.values().length)]);
                    diagnosis.setCreatedAt(LocalDateTime.now());
                    diagnosis.setUpdatedAt(LocalDateTime.now());
                    diagnosisRepository.save(diagnosis);
                }

                // Add 0-2 prescriptions per visit
                int numPrescriptions = random.nextInt(3);
                for (int p = 0; p < numPrescriptions; p++) {
                    String medication = medications[random.nextInt(medications.length)];
                    Prescription prescription = new Prescription();
                    prescription.setVisit(visit);
                    prescription.setMedicationName(medication);
                    prescription.setDosage("10mg");
                    prescription.setFrequency("Once daily");
                    prescription.setDurationDays(30);
                    prescription.setQuantity(30);
                    prescription.setInstructions("Take with food");
                    prescription.setStatus(Prescription.PrescriptionStatus.ACTIVE);
                    prescription.setCreatedAt(LocalDateTime.now());
                    prescription.setUpdatedAt(LocalDateTime.now());
                    prescriptionRepository.save(prescription);
                }
            }
        }
    }
}
