package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class DoctorRegistry {

    @Id
    private String id;
    private String doctorId;
    private String doctorEmail;
    private List<ScheduleRegistry> scheduleRegistries;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
