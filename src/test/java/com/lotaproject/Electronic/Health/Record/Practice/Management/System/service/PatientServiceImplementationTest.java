package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.PaginatedPatientResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.CannotRegisterPatientException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
@SpringBootTest
class PatientServiceImplementationTest {
    @Autowired
    private PatientService patientService;
    @BeforeEach
    void setUp() {
    }
    @Test
    void testThatCanRegisterPatient() throws TemplateException, IOException {
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

//        Patient patient = Patient.builder().

        RegisterPatientRequest request = RegisterPatientRequest.builder()
                .gender("MALE")
                .email("ezeirunnechiamaka123@gmail.com")
                .password("#Rems2222")
                .address("9 road")
                .guardian("mr him")
                .firstName("Paulinus")
                .lastName("Nri")
                .phoneNumber("080343332")
                .genotype("AA")
                .bloodGroup("O_POSITIVE")
                .occupation("intro tech")
                .dob(String.valueOf(LocalDate.of(2000,3,22)))
                .guardianPhoneNumber("0909090")
                .medicalHistory(medicalHistory)
                .build();
        ApiResponse<?> response = patientService.registerPatient(request);

            assertAll(
                    ()-> assertNotNull(response),
                    ()-> assertThat(response.getMessage(), is("Registration Successfully"))
            );
    }
    @Test
    @DisplayName("Throw exception if email already exist")
    void testThatCannotRegisterPatient(){

        RegisterPatientRequest request = RegisterPatientRequest.builder()
                .gender("MALE")
                .email("lolo@gmail.com")
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
                .build();

        assertThrows(CannotRegisterPatientException.class, ()-> patientService.registerPatient(request));

    }
    @Test
    @DisplayName("Throw exception if email is not a valid one")
    void testThatCannotRegisterPatient2(){

        RegisterPatientRequest request = RegisterPatientRequest.builder()
                .gender("MALE")
                .email(".lolo@gmail.com")
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
                .build();

        assertThrows(CannotRegisterPatientException.class, ()-> patientService.registerPatient(request));

    }
    @Test
    void testThatCanFindPatientByEmail(){

        Patient patient = patientService.findByEmail("amaka@gmail.com");
        assertAll(
                ()-> assertNotNull(patient),
                ()->assertEquals(patient.getFirstName(), "lota")
        );
    }
    @Test
    void testThatCannotFindUserByEmail_if_email_doesnot_exist_in_database(){

        assertThrows(PatientDoesNotexistException.class, ()-> patientService.findByEmail("dsddssamaka@gmail.com"));
    }

    @Test
    void testThatCanViewPatientById(){

        Patient patient = patientService.findById("649c9fe055fa5534e308b2da");
        assertAll(
                ()-> assertNotNull(patient),
                ()->assertEquals(patient.getFirstName(), "lota")
        );
    }
    @Test
    void testThatCannotFindUserById_if_id_doesnot_exist_in_database(){

        assertThrows(PatientDoesNotexistException.class, ()-> patientService.findById("364723793274883"));
    }
    @Test
    void testThatCanUpdatePatientDetails(){

        UpdatePatientDetailRequest request1 = new UpdatePatientDetailRequest();
        request1.setEmail("amaka@gmail.com");

        ApiResponse<?> response1 = patientService.updatePatientDetails("64a332cc0003081a15b23893", request1);
        assertThat(response1.getMessage(), is("Updated Successful"));

    }
    @Test
    @DisplayName("throw exception if email is not valid")
    void testThatCannotUpdatePatientDetails1(){

        UpdatePatientDetailRequest request1 = new UpdatePatientDetailRequest();
        request1.setEmail("*amaka@gmail.com");

        assertThrows(CannotRegisterPatientException.class, ()-> patientService.updatePatientDetails("64a332cc0003081a15b23893", request1));
    }
    @Test
    @DisplayName("throw exception if email is already exists")
    void testThatCannotUpdatePatientDetails2(){

        UpdatePatientDetailRequest request1 = new UpdatePatientDetailRequest();
        request1.setEmail("lotas@gmail.com");

        assertThrows(CannotRegisterPatientException.class, ()-> patientService.updatePatientDetails("64a332cc0003081a15b23893", request1));
    }
    @Test
    void testThatCanFindPatientByName(){
        PaginatedPatientResponse response = patientService.findByName(1,10,"ify");

        assertAll(
                ()-> assertThat(response.getNoOfPatients(), is(1))
        );
    }


    @AfterEach
    void tearDown() {
    }
}