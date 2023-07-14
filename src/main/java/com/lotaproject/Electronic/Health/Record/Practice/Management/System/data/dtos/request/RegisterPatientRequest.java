package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.BloodGroup;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Gender;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Genotype;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String occupation;
    private String dob;
    private String gender;
    private MedicalHistory medicalHistory;
    private String bloodGroup;
    private String genotype;
    private String guardian;
    private String guardianPhoneNumber;

}
