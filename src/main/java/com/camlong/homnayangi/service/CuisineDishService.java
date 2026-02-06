package com.camlong.homnayangi.service;

import com.camlong.homnayangi.entity.CuisineDish;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface CuisineDishService {

  CuisineDish findById(Long id);

  CuisineDish createNewDish(CuisineDish cuisineDish);

  List<CuisineDish> findAll();

  void patchDish(Long id, JsonNode cuisineDish);
}
