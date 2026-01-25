package com.camlong.homnayangi.application.usermangement;

import com.camlong.homnayangi.domain.models.ApplicationRefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

  Optional<ApplicationRefreshToken> findByToken(String token);
  void deleteByUsername(String username);
  ApplicationRefreshToken save(ApplicationRefreshToken refreshToken);
}
