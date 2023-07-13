package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponse {

    private String jwt;
//    private String token;
    private String refreshToken;
    private List<String> roles;
    private String email;
    private String id;
}
