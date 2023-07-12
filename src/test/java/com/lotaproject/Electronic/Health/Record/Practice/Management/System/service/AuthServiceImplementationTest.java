package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplementationTest {

    @Autowired
    private AuthService authService;

    @Test
    void testThatUserCanLogin(){

        LoginRequest request = LoginRequest.builder()
                .email("ceo@gmail.com")
                .password("#Passord123")
                .build();

        LoginResponse response = authService.login(request);

    }

}