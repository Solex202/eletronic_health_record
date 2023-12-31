package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Doctor {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String uniqueId;
    private LocalDateTime registeredDate;
    private LocalDateTime modifiedDate;
    private Set<Role> roles;
//    private List<AppointmentForm> appointments;
}
