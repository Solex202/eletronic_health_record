package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register() {
        MedicalHistory medicalHistory = new MedicalHistory();
        List<String> ailments = new ArrayList<>();
        ailments.add("headaches");
        ailments.add("sickness");

        List<String> allergy = new ArrayList<>();
        allergy.add("house dust");
        allergy.add("insect sting");

        List<String> medication = new ArrayList<>();
        medication.add("medicine");

        medicalHistory.setAilment(ailments);
        medicalHistory.setAllergy(allergy);
        medicalHistory.setMedication(medication);

        RegisterPatientRequest request = RegisterPatientRequest.builder()
                .gender("MALE")
                .email("remi@gmail.com")
                .password("#Rems2222")
                .address("9 road")
                .guardian("mr him")
                .firstName("lota")
                .lastName("chi")
                .phoneNumber("080343332")
                .genotype("AA")
                .bloodGroup("O_POSITIVE")
                .occupation("intro tech")
                .dob(String.valueOf(LocalDate.of(2000,3,22)))
                .guardianPhoneNumber("0909090")
                .medicalHistory(medicalHistory)
                .build();

//        Patient patient = patientRepository.save(request);

        // Mock the sampleService to return the created sample when createSample() is called
//        when(patientService.registerPatient(request)).thenReturn(request);

        // Call the controller method
//        ResponseEntity<ApiResponse<?>> response = patientController.register(request);

        // Verify the response
//        assert response.getStatusCode() == HttpStatus.CREATED;
//        assert response.getBody() == request;

        // Verify that the sampleService.createSample() method was called once with the correct argument
        verify(patientService, times(1)).registerPatient(request);
    }


    @Test
    void login() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void findByName() {
    }
}