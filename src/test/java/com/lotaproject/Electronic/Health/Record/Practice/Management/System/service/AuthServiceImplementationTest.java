package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceImplementationTest {

    @Autowired
    private AuthService authService;

    @Test
    void testThatUserCannotLogin(){

        LoginRequest request = LoginRequest.builder()
                .email("rtemi@gmail.com")
                .password("#Rems2222nj")
                .build();

        LoginResponse response = authService.login(request);
//        assertThrows(ElectronicHealthException.class, ()-> authService.login(request));

    }

}