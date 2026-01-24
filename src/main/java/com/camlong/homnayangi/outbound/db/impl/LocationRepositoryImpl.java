package com.camlong.homnayangi.outbound.db.impl;

import com.camlong.homnayangi.application.constants.District;
import com.camlong.homnayangi.application.domain.models.Location;
import com.camlong.homnayangi.application.location.LocationRepository;
import com.camlong.homnayangi.outbound.db.LocationJpaRepository;
import com.camlong.homnayangi.outbound.db.entities.LocationEntity;
import com.camlong.homnayangi.outbound.db.mappers.LocationJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final LocationJpaRepository jpaRepository;
    private final LocationJpaMapper mapper;

    @Override
    public Optional<Location> findLocationById(Long id) {
        final Optional<LocationEntity> entity = jpaRepository.findById(id);
        return entity.map(mapper::toLocation);
    }

    @Override
    public List<Location> findAllLocationsByDistrictAndNameContains(District district, String name) {
        // TODO: implement later
        return List.of();
    }

    @Override
    public Boolean saveLocation(Location location) {
        final LocationEntity toSaveEntity = mapper.toLocationEntity(location);
        jpaRepository.save(toSaveEntity);
        return true;
    }

    @Override
    public Boolean saveAllLocations(List<Location> locations) {
        final List<LocationEntity> toSaveEntities = locations.stream().map(mapper::toLocationEntity).toList();
        jpaRepository.saveAll(toSaveEntities);
        return true;
    }
}
