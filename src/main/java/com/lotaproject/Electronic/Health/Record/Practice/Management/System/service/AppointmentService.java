package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    ApiResponse<?> bookAppointment(String id, BookAppointmentFormDto form);

    List<String> getAvailableDoctors(LocalDate date);

    List<LocalTime> getDoctorTimeSlots(String doctorName, String date);

    ApiResponse<?> rescheduleAppointment( String appointmentId, BookAppointmentFormDto form);
}
