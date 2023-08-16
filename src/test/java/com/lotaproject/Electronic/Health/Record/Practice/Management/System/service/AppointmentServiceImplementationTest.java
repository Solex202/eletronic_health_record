package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.AppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class AppointmentServiceImplementationTest {

    @Autowired
    private  AppointmentService appointmentService;


    @Test
    void testThatCanBookAppointment(){
        AppointmentFormDto form = new AppointmentFormDto();
        form.setAppointmentDate(LocalDate.of(2023, 7,21));
        form.setDoctorName("Doctor jesus");
        form.setAppointmentTime(LocalTime.of(9, 0));

        ApiResponse<?> response = appointmentService.bookAppointment("64a332cc0003081a15b23893", form);

        assertThat(response.getMessage(), is("Appointment Booked successfully"));
    }

    @Test
    void testThatCanGetDoctorsAvailableInADay(){
        List<String> res = appointmentService.availableDoctors(LocalDate.of(2023,9,26));
    }
}