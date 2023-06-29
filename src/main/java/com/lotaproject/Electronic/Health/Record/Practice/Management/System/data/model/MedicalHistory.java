package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class MedicalHistory {

    @Id
    private String id;
//    @DBRef
//    private String patientId;
    private List<String> ailment;
    private List<String> allergy;
    private List<String> attachment;
    private List<String> medication;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;



}
