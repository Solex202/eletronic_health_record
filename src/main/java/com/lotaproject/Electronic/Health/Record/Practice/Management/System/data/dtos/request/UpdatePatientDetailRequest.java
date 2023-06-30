package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.BloodGroup;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Gender;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Genotype;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UpdatePatientDetailRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String occupation;
    private String dob;
    private String gender;
    private String bloodGroup;
    private String genotype;
    private String guardian;
    private String guardianPhoneNumber;
}
