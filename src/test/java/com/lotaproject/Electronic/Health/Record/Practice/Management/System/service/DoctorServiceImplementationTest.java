package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class DoctorServiceImplementationTest {
    @Autowired
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testThatCanCreateDoctor() throws TemplateException, IOException {
        Doctor doctor = Doctor.builder().firstName("deji").lastName("dee").password("#1Ppassword").email("deolaoladeji@gmail.com").build();
        ApiResponse<?> response = doctorService.saveDoctor(doctor);

        assertAll(
                ()-> assertThat(response.getMessage(), is("Successful"))
        );
    }

    @Test
    void findAll(){
        List<Doctor> doctors = doctorService.findAllDoctors();
        assertThat(doctors.size(), is(5));
    }

    @Test
    void deleteDoctor(){
        String res = doctorService.deleteDoctorFromDatabase("64e2182c6eb62a3cba1c9fb3");
    }

    @AfterEach
    void tearDown() {
    }
}