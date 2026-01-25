package com.camlong.homnayangi.outbound.db.mappers;

import com.camlong.homnayangi.application.domain.models.ApplicationRefreshToken;
import com.camlong.homnayangi.outbound.db.entities.RefreshTokenEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CommonJpaMapper {

  ApplicationRefreshToken toApplicationRefreshToken(final RefreshTokenEntity entity);

  RefreshTokenEntity toEntity(final ApplicationRefreshToken refreshToken);
}
