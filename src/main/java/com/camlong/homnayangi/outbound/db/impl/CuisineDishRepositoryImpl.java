package com.camlong.homnayangi.outbound.db.impl;

import com.camlong.homnayangi.application.cuisine.CuisineDishRepository;
import com.camlong.homnayangi.domain.models.cusine.CuisineDish;
import com.camlong.homnayangi.domain.models.cusine.CuisineDishFilter;
import com.camlong.homnayangi.outbound.db.CuisineDishJpaRepository;
import com.camlong.homnayangi.outbound.db.entities.CuisineDishEntity;
import com.camlong.homnayangi.outbound.db.mappers.CuisineDishJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CuisineDishRepositoryImpl implements CuisineDishRepository {

  private final CuisineDishJpaRepository jpaRepository;
  private final CuisineDishJpaMapper mapper;

  @Override
  public Optional<CuisineDish> findById(final Long id) {
    final Optional<CuisineDishEntity> entity = jpaRepository.findById(id);
    return entity.map(mapper::toCuisineDish);
  }

  @Override
  public void save(final CuisineDish cuisineDish) {
    final CuisineDishEntity entity = mapper.toEntity(cuisineDish);
    jpaRepository.save(entity);
  }

  @Override
  public List<CuisineDish> findAll() {
    final List<CuisineDishEntity> allEntities = jpaRepository.findAll();
    return allEntities.stream().map(mapper::toCuisineDish).toList();
  }

  @Override
  public List<CuisineDish> findAllWithFilter(CuisineDishFilter filter) {
    return List.of();
  }

  @Override
  public void delete(final Long id) {
    jpaRepository.deleteById(id);
  }
}
