package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    ApiResponse<?> bookAppointment(String patientId, BookAppointmentFormDto form) throws IOException, TemplateException;

    List<String> getAvailableDoctors(LocalDate date);

    List<LocalTime> getDoctorTimeSlots(String doctorName, String date);

    ApiResponse<?> rescheduleAppointment(String patientId, String appointmentId, BookAppointmentFormDto form) throws TemplateException, IOException;

    AppointmentForm cancelAppointment(String id);

    AppointmentForm viewAppointment(String id);

    List<AppointmentForm> findAll();

}
