package com.camlong.homnayangi.application.cuisine;

import com.camlong.homnayangi.domain.models.cusine.CuisineDish;

public interface CuisineDishService {

  CuisineDish findById(Long id);

  void save(CuisineDish cuisineDish);
}
