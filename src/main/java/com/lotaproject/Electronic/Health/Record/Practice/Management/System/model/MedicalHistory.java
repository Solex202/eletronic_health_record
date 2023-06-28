package com.lotaproject.Electronic.Health.Record.Practice.Management.System.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalHistory {

    private String id;
    @DBRef
    private String patientId;
    private List<String> ailment;
    private List<String> allergy;
    private List<String> attachment;
    private List<String> medication;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;



}
