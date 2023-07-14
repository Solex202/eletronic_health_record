package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.PatientService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
//    @Autowired
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
    void create() throws Exception {

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
        Patient patient = new Patient();

        patient.setMedicalHistory(medicalHistory);
        patient.setRoles(Set.of(Role.PATIENT));
        patient.setPatientId("patientIdentity");
        patient.setAddress(request.getAddress());
        patient.setEmail(request.getEmail());
        patient.setPassword("#Rems2222");
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setOccupation(request.getOccupation());
        patient.setGender(Gender.valueOf(request.getGender()));
        patient.setBloodGroup(BloodGroup.valueOf(request.getBloodGroup()));
        patient.setGenotype(Genotype.valueOf(request.getGenotype()));
        patient.setDob(LocalDate.parse(request.getDob()));
        patient.setGuardian(request.getGuardian());
        patient.setGuardianPhoneNumber(request.getGuardianPhoneNumber());
        patient.setRegisteredDate(LocalDateTime.now());
        patient.setModifiedDate(LocalDateTime.now());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/health-record/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                        .andExpect(status().isCreated());

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

            Patient patient = new Patient();

            patient.setMedicalHistory(medicalHistory);
            patient.setRoles(Set.of(Role.PATIENT));
            patient.setPatientId("patientIdentity");
            patient.setAddress(request.getAddress());
            patient.setEmail(request.getEmail());
            patient.setPassword("encodedPassword");
            patient.setFirstName(request.getFirstName());
            patient.setLastName(request.getLastName());
            patient.setPhoneNumber(request.getPhoneNumber());
            patient.setOccupation(request.getOccupation());
            patient.setGender(Gender.valueOf(request.getGender()));
            patient.setBloodGroup(BloodGroup.valueOf(request.getBloodGroup()));
            patient.setGenotype(Genotype.valueOf(request.getGenotype()));
            patient.setDob(LocalDate.parse(request.getDob()));
            patient.setGuardian(request.getGuardian());
            patient.setGuardianPhoneNumber(request.getGuardianPhoneNumber());
            patient.setRegisteredDate(LocalDateTime.now());
            patient.setModifiedDate(LocalDateTime.now());

        // Set the required fields in the saved patient

            ApiResponse<?> expectedResponse = ApiResponse.builder()
                    .message("Registration Successfully")
                    .data(patient)
                    .build();

//            when(patientRegistrationService.registerPatient(any())).thenReturn()

            mockMvc.perform(post("/health-record/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Registration Successfully"))
                    .andExpect(jsonPath("$.data").exists())
                    .andExpect(jsonPath("$.data.patientId").value(patient.getPatientId()));

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