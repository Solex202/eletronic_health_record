package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.AppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    ApiResponse<?> bookAppointment(String id, AppointmentFormDto form);

    List<String> getAvailableDoctors(LocalDate date);

    List<LocalTime> getDoctorTimeSlots(String doctorName);
}
