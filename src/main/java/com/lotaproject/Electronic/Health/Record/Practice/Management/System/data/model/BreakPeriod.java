package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreakPeriod {

    @Id
    private String id;
    private LocalTime from;
    private  LocalTime to;
}
