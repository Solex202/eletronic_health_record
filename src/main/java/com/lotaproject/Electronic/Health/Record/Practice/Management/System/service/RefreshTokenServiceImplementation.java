package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.RefreshTokenRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.TokenRefreshException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.security.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImplementation implements RefreshTokenService{

    @Value("${health.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setPatient(patientRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

}
