package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
