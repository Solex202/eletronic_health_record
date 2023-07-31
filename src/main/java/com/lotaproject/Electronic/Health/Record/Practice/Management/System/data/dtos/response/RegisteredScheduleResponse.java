package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisteredScheduleResponse {
    private String doctorId;
    private String doctorEmail;
    private String doctorName;
    private LocalDate date;

    private List<LocalTime> intervals;

}
