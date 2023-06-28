package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MedicalHistoryDto {

    private String patientId;
    private List<String> ailment;
    private List<String> allergy;
    private List<String> attachment;
    private List<String> medication;
    private LocalDateTime modifiedDate;
}
