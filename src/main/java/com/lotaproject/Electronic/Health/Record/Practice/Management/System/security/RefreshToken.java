package com.lotaproject.Electronic.Health.Record.Practice.Management.System.security;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
public class RefreshToken {

    @Id
    private String id;

    private Patient patient;

    private String token;

    private Instant expiryDate;
}
