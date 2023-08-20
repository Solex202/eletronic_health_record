package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentForm {

    @Id
    private String id;
    private String appointmentDate;
    private String appointmentTime;
    private String patientID;
    private String patientName;
    private String doctorName;
    private LocalDateTime bookedTime;
    private LocalDateTime modifiedDate;
    private AppointmentStatus appointmentStatus;
    private String duration;
//    and other relevant details
}
