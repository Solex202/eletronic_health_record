package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class AppointmentServiceImplementationTest {

    @Autowired
    private  AppointmentService appointmentService;


    @Test
    void testThatCanBookAppointment(){
        BookAppointmentFormDto form = new BookAppointmentFormDto();
        form.setAppointmentDate(LocalDate.of(2023, 7,21));
        form.setDoctorName("Doctor jesus");
        form.setAppointmentTime(LocalTime.of(9, 0));

        ApiResponse<?> response = appointmentService.bookAppointment("64a332cc0003081a15b23893", form);

        assertThat(response.getMessage(), is("Appointment Booked successfully"));
    }

    @Test
    void testThatCanGetDoctorsAvailableInADay(){
        List<String> res = appointmentService.getAvailableDoctors(LocalDate.of(2023,9,23));
        assertThat(res.size(), is(1));
    }

    @Test
    void testThatCanGetDoctorAvailableTimes(){
        List<LocalTime> res = appointmentService.getDoctorTimeSlots("ademiju@gmail.com", "2023-09-25");
    }

    @Test
    void rescheduleAppointment(){
        BookAppointmentFormDto form = new BookAppointmentFormDto();
        form.setAppointmentDate(LocalDate.of(2023, 7,21));
        form.setDoctorName("Doctor jesus");
        form.setAppointmentTime(LocalTime.of(9, 0));

        ApiResponse<?> response = appointmentService.bookAppointment("64a332cc0003081a15b23893", form);

        assertThat(response.getMessage(), is("Appointment Booked successfully"));
    }
}