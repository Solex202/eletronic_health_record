package com.lotaproject.Electronic.Health.Record.Practice.Management.System.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

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
    private String phoneNumber;
    private String address;
    private String occupation;
    private LocalDate dob;
    private Gender gender;
    private BloodGroup bloodGroup;
    private Genotype genotype;
    private String registeredDate;
    private String modifiedData;
    private String guardian;

}
