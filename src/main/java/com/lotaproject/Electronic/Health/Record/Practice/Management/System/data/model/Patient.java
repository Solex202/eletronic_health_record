package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String occupation;
    private LocalDate dob;
    private Gender gender;
    @DBRef
    private MedicalHistory medicalHistory;
    private BloodGroup bloodGroup;
    private Genotype genotype;
    private LocalDateTime registeredDate;
    private LocalDateTime modifiedDate;
    private String guardian;
    private String guardianPhoneNumber;
    private String patientId;
    private  boolean loginStatus;
    private Set<Role> roles;

}
