package com.camlong.homnayangi.outbound.db.mappers;

import com.camlong.homnayangi.domain.models.ApplicationUser;
import com.camlong.homnayangi.outbound.db.entities.ApplicationUserEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true)
)
public interface ApplicationUserJpaMapper {

    ApplicationUser toApplicationUser(final ApplicationUserEntity entity);

    ApplicationUserEntity toEntity(final ApplicationUser applicationUser);
}
