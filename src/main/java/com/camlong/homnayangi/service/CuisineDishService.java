package com.camlong.homnayangi.service;

import com.camlong.homnayangi.entity.CuisineDish;

import java.util.List;
import java.util.Map;

public interface CuisineDishService {

  CuisineDish findById(Long id);

  CuisineDish createNewDish(CuisineDish cuisineDish);

  List<CuisineDish> findAll();

  void patchDish(Long id, Map<String, Object> cuisineDish);
}
