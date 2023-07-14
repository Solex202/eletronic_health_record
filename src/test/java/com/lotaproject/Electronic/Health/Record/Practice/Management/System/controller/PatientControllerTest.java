package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.PatientService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PatientService patientRegistrationService;

    @Before
    public void setup(WebApplicationContext wac) {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register() throws Exception{

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
            // Set the required fields in the request

            Patient savedPatient = new Patient();
            // Set the required fields in the saved patient

            ApiResponse<?> expectedResponse = ApiResponse.builder()
                    .message("Registration Successfully")
                    .data(savedPatient)
                    .build();

//            when(patientRegistrationService.registerPatient(request)).thenReturn(expectedResponse);

            mockMvc.perform(post("/health-record/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Registration Successfully"))
                    .andExpect(jsonPath("$.data").exists())
                    .andExpect(jsonPath("$.data.patientId").value(savedPatient.getPatientId()));

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