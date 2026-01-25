package com.camlong.homnayangi.application.cuisine.impl;

import com.camlong.homnayangi.application.cuisine.CuisineDishRepository;
import com.camlong.homnayangi.application.cuisine.CuisineDishService;
import com.camlong.homnayangi.domain.models.cusine.CuisineDish;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

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
    cuisineDish.initCreatedDomainModel();
    return repository.save(cuisineDish);
  }

  @Override
  public List<CuisineDish> findAll() {
    return repository.findAll();
  }

  @Override
  public void patchDish(Long id, JsonNode request) {
    final CuisineDish existingDish = repository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));

    applyTextField(request, "name", existingDish::setName);
    applyTextField(request, "type", existingDish::setType);
    applyTextField(request, "culture", existingDish::setCulture);
    applyTextField(request, "imageUrl", existingDish::setImageUrl);

    repository.save(existingDish);
  }

  private void applyTextField(JsonNode node, String field, Consumer<String> setter) {
    if (node == null || !node.has(field)) return;
    com.fasterxml.jackson.databind.JsonNode valueNode = node.get(field);
    if (valueNode == null || valueNode.isNull()) return;
    String value = valueNode.asText("");
    if (!value.isBlank()) {
      setter.accept(value.trim());
    }
  }

}
