package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentForm {

   private LocalDate appointmentDate;
    private LocalTime timeSlot;
    private String patientID;
    private String patientName;
    private String doctorName;
    private LocalDateTime bookedTime;
//    and other relevant details
}
