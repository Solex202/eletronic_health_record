package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.security.RefreshToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenImplementationTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByToken() {
    }

    @Test
    void createRefreshToken() {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken("64af29cccc01ee6db0b52fcf");
    }

    @Test
    void verifyExpiration() {
    }
}