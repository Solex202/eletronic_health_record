package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.security.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
}
