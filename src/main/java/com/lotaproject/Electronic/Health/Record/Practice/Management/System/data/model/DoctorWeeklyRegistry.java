package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class DoctorWeeklyRegistry {

    @Id
    private String id;
    private List<LocalTime> availableTimes;
    private List<LocalDate> availableDates;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
