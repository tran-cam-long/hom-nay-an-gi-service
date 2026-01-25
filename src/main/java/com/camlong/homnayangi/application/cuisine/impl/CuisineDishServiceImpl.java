package com.camlong.homnayangi.application.cuisine.impl;

import com.camlong.homnayangi.application.cuisine.CuisineDishRepository;
import com.camlong.homnayangi.application.cuisine.CuisineDishService;
import com.camlong.homnayangi.domain.models.cusine.CuisineDish;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuisineDishServiceImpl implements CuisineDishService {

  private final CuisineDishRepository repository;

  @Override
  public CuisineDish findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Dish was not found"));
  }

  @Override
  public void save(CuisineDish cuisineDish) {
    repository.save(cuisineDish);
  }
}
