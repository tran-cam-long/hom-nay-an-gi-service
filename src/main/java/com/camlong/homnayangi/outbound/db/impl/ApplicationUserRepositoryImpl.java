package com.camlong.homnayangi.outbound.db.impl;

import com.camlong.homnayangi.domain.models.ApplicationUser;
import com.camlong.homnayangi.application.usermangement.ApplicationUserRepository;
import com.camlong.homnayangi.outbound.db.ApplicationUserJpaRepository;
import com.camlong.homnayangi.outbound.db.entities.ApplicationUserEntity;
import com.camlong.homnayangi.outbound.db.mappers.ApplicationUserJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationUserRepositoryImpl implements ApplicationUserRepository {

    private final ApplicationUserJpaRepository jpaRepository;
    private final ApplicationUserJpaMapper mapper;

    @Override
    public Optional<ApplicationUser> findById(final Long id) {
        final Optional<ApplicationUserEntity> entity = jpaRepository.findById(id);
        return entity.map(mapper::toApplicationUser);
    }

    @Override
    public Optional<ApplicationUser> findByUsername(final String username) {
        final Optional<ApplicationUserEntity> entity = jpaRepository.findByUsername(username);
        return entity.map(mapper::toApplicationUser);
    }

    @Override
    public boolean save(ApplicationUser applicationUser) {
        final ApplicationUserEntity toSaveEntity = mapper.toEntity(applicationUser);
        jpaRepository.save(toSaveEntity);
        return true;
    }
}
