package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.AppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    ApiResponse<?> bookAppointment(String id, AppointmentFormDto form);

    List<String> availableDoctors(LocalDate date);
}
