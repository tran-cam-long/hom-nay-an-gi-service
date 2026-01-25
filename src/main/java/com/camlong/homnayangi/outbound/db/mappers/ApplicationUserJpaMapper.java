package com.camlong.homnayangi.outbound.db.mappers;

import com.camlong.homnayangi.application.domain.models.ApplicationUser;
import com.camlong.homnayangi.outbound.db.entities.ApplicationUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationUserJpaMapper {

    ApplicationUser toApplicationUser(final ApplicationUserEntity entity);

    ApplicationUserEntity toEntity(final ApplicationUser applicationUser);
}
