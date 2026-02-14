package com.camlong.homnayangi.repository;

import com.camlong.homnayangi.entity.RefreshToken;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken, @NonNull Long> {
  Optional<RefreshToken> findByToken(String token);

  void deleteByUsername(String username);

  void deleteByToken(String token);
}
