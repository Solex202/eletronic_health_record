package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentStatus;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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
    void testThatCanBookAppointment() throws TemplateException, IOException {
        BookAppointmentFormDto form = new BookAppointmentFormDto();
        form.setAppointmentDate(LocalDate.of(2023, 9,20));
        form.setDoctorName("miju ade");
        form.setAppointmentTime(LocalTime.of(4  , 0));

        ApiResponse<?> response = appointmentService.bookAppointment("64c24f02b938fe00ef5177ae", form);

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
        assertThat(res.size(), is(5));
    }

    @Test
    void rescheduleAppointment() throws TemplateException, IOException {
        BookAppointmentFormDto form = new BookAppointmentFormDto();
        form.setAppointmentDate(LocalDate.of(2023, 7,25));
        form.setDoctorName("Doctor jesus");
        form.setAppointmentTime(LocalTime.of(11, 30));

        ApiResponse<?> response = appointmentService.rescheduleAppointment("64a332cc0003081a15b23893","64e2182c6eb62a3cba1c9fb3", form);

        assertThat(response.getMessage(), is("Appointment rescheduled successfully"));
    }

    @Test
    void cancelAppointment(){
        AppointmentForm appointment = appointmentService.cancelAppointment("64e36dbc1fc83f4d67a77473");

        assertThat(appointment.getAppointmentStatus(),is(AppointmentStatus.CANCELLED));
    }

    @Test
    void viewAppointment(){
        AppointmentForm appointmentForm = appointmentService.viewAppointment("64e36dbc1fc83f4d67a77473");
    }

    @Test
    void findAll(){
        List<AppointmentForm> appointmentFormList = appointmentService.findAll();
    }
}