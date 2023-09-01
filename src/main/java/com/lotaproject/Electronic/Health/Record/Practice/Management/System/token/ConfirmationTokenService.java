package com.lotaproject.Electronic.Health.Record.Practice.Management.System.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        confirmationTokenRepository.save(confirmationToken);

    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {

        ConfirmationToken token1 = getToken(token).get();
        token1.setConfirmedAt(LocalDateTime.now());

        confirmationTokenRepository.save(token1);


    }
}
