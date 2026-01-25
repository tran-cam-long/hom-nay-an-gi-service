package com.camlong.homnayangi.outbound.db.impl;

import com.camlong.homnayangi.domain.models.ApplicationRefreshToken;
import com.camlong.homnayangi.application.usermangement.RefreshTokenRepository;
import com.camlong.homnayangi.outbound.db.RefreshTokenJpaRepository;
import com.camlong.homnayangi.outbound.db.entities.RefreshTokenEntity;
import com.camlong.homnayangi.outbound.db.mappers.CommonJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

  private final RefreshTokenJpaRepository repository;
  private final CommonJpaMapper mapper;

  @Override
  public Optional<ApplicationRefreshToken> findByToken(String token) {
    final Optional<RefreshTokenEntity> entity = repository.findByToken(token);
    return entity.map(mapper::toApplicationRefreshToken);
  }

  @Override
  public void deleteByUsername(String username) {
    repository.deleteByUsername(username);
  }

  @Override
  public ApplicationRefreshToken save(ApplicationRefreshToken refreshToken) {
    final RefreshTokenEntity toSave = mapper.toEntity(refreshToken);
    return mapper.toApplicationRefreshToken(repository.save(toSave));
  }
}
