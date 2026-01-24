package com.camlong.homnayangi.outbound.db.mappers;

import com.camlong.homnayangi.application.domain.models.Location;
import com.camlong.homnayangi.outbound.db.entities.LocationEntity;
import org.mapstruct.Mapper;

@Mapper
public interface LocationJpaMapper {

    Location toLocation(final LocationEntity entity);

    LocationEntity toLocationEntity(final Location location);
}
