package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MedicalHistoryServiceImplementationTest {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testThatCan(){

        MedicalHistory medicalHistory = new MedicalHistory();
        List<String> ailments = new ArrayList<>();
        ailments.add("headaches");
        ailments.add("sickness");

        List<String> allergy = new ArrayList<>();
        allergy.add("house dust");
        allergy.add("insect sting");

        List<String> medication = new ArrayList<>();
        medication.add("medicine");

        List<String> attachment = new ArrayList<>();
        attachment.add("pics 1");

        medicalHistory.setAilment(ailments);
        medicalHistory.setAllergy(allergy);
        medicalHistory.setMedication(medication);
        medicalHistory.setAttachment(attachment);

        medicalHistoryService.createMedicalHistory(medicalHistory);

    }

    @AfterEach
    void tearDown() {
    }
}