package com.lotaproject.Electronic.Health.Record.Practice.Management.System.token;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ConfirmToken {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new ElectronicHealthException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ElectronicHealthException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ElectronicHealthException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
//        appUserService.enableAppUser(
//                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
