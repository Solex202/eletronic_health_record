package com.lotaproject.Electronic.Health.Record.Practice.Management.System.token;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfirmationTokenServiceTest {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Test
    void saveConfirmationToken() {

        ConfirmationToken token =  new ConfirmationToken();

    }

    @Test
    void getToken() {
    }

    @Test
    void setConfirmedAt() {
    }
}