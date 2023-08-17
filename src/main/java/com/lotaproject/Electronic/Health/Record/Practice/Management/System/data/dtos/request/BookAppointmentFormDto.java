package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentFormDto {

    private LocalTime appointmentTime;
    private String doctorName;
    private LocalDate appointmentDate;
}
