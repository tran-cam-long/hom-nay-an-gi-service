package com.camlong.homnayangi.application.usermangement.impl;

import com.camlong.homnayangi.application.domain.models.ApplicationRefreshToken;
import com.camlong.homnayangi.application.usermangement.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository repository;

  @Value("${jwt.refresh-expiration}")
  private long refreshExpiration;

  public ApplicationRefreshToken create(String username, String token) {
    final ApplicationRefreshToken refreshToken = ApplicationRefreshToken.builder()
        .username(username).token(token).expiryTime(Instant.now().plusMillis(refreshExpiration)).build();

    return repository.
  }
}
