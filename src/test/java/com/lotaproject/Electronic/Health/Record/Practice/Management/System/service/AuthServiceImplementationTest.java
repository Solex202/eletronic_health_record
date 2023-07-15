package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.AuthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
@SpringBootTest
class AuthServiceImplementationTest {

    @Autowired
    private AuthService authService;

    @Test
    void testThatUserCanLogin(){

        LoginRequest request = LoginRequest.builder()
                .email("fer@gmail.com")
                .password("#Rems2222")
                .build();

        LoginResponse response = authService.login(request);
        assertAll(
                ()-> assertThat(response.getEmail(), is("fer@gmail.com"))
        );
//        assertThrows(ElectronicHealthException.class, ()-> authService.login(request));

    }

    @Test
    void testThatUserCannotLogin(){

        LoginRequest request = LoginRequest.builder()
                .email("ferw@gmail.com")
                .password("#Rems2222")
                .build();

        assertThrows(AuthException.class, ()-> authService.login(request));

    }

}