package com.lotaproject.Electronic.Health.Record.Practice.Management.System.token;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor
//@AllArgsConstructor
@Data
public class ConfirmationToken {

    @Id
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @DBRef
    private Patient patient;

    @DBRef
    private Doctor doctor;
    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, LocalDateTime confirmedAt, Patient patient) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt=confirmedAt;
        this.patient = patient;
    }

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt,LocalDateTime confirmedAt, Doctor doctor) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt=confirmedAt;
        this.doctor = doctor;
    }

}
