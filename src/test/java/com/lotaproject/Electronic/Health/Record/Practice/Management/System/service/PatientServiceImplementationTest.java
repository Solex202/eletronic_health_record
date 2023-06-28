package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.MedicalHistoryDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest
@AllArgsConstructor
class PatientServiceImplementationTest {

    private final PatientService patientService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testThatCanRegisterPatient(){
        MedicalHistoryDto dto = new MedicalHistoryDto();
        List<String> ailments = new ArrayList<>();
        ailments.add("headaches");
        ailments.add("sickness");

        List<String> allergy = new ArrayList<>();
        allergy.add("house dust");
        allergy.add("insect sting");

        List<String> medication = new ArrayList<>();
        medication.add("medicine");

        dto.setAilment(ailments);
        dto.setAllergy(allergy);
        dto.setMedication(medication);

        RegisterPatientRequest request = RegisterPatientRequest.builder()
                .gender("male")
                .email("lota@gmail.com")
                .address("9 road")
                .guardian("mr him")
                .firstName("lota")
                .lastName("chi")
                .phoneNumber("080343332")
                .genotype("aa")
                .bloodGroup("O_POSITIVE")
                .occupation("intro tech")
                .dob(String.valueOf(LocalDate.of(2000,3,22)))
                .guardianPhoneNumber("0909090")
                .medicalHistoryDto(dto)
                .build();

        ApiResponse<?> response = patientService.registerPatient(request);

            assertAll(
                    ()-> assertNotNull(response)
            );
    }

    @AfterEach
    void tearDown() {
    }
}