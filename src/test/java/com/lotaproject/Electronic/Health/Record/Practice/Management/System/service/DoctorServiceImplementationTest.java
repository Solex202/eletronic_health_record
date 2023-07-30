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
        Doctor doctor = Doctor.builder().firstName("quackish").lastName("doc").password("#1Ppassword").email("doctors@gmail.com").build();
        ApiResponse<?> response = doctorService.saveDoctor(doctor);

        assertAll(
                ()-> assertThat(response.getMessage(), is("Successful"))
        );
    }

    @AfterEach
    void tearDown() {
    }
}