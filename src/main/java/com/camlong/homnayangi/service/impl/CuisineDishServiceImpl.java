package com.camlong.homnayangi.service.impl;

import com.camlong.homnayangi.entity.CuisineDish;
import com.camlong.homnayangi.repository.CuisineDishRepository;
import com.camlong.homnayangi.service.CuisineDishService;
import com.camlong.homnayangi.utils.JsonNodeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
  public CuisineDish createNewDish(CuisineDish cuisineDish) {
    cuisineDish.initToBeCreatedEntity();
    return repository.save(cuisineDish);
  }

  @Override
  public List<CuisineDish> findAll() {
    return repository.findAll();
  }

  @Override
  public void patchDish(Long id, JsonNode request) {
    final CuisineDish existingDish = repository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));

    JsonNodeUtils.applyTextField(request, "name", existingDish::setName);
    JsonNodeUtils.applyTextField(request, "type", existingDish::setType);
    JsonNodeUtils.applyTextField(request, "culture", existingDish::setCulture);
    JsonNodeUtils.applyTextField(request, "imageUrl", existingDish::setImageUrl);

    repository.save(existingDish);
  }

}
