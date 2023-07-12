package com.lotaproject.Electronic.Health.Record.Practice.Management.System.security;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import lombok.Data;

import java.time.Instant;

@Data
public class RefreshToken {

    private long id;

    private Patient patient;

    private String token;

    private Instant expiryDate;
}
