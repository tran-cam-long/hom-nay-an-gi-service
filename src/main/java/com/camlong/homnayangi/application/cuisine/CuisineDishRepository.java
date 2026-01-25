package com.camlong.homnayangi.application.cuisine;

import com.camlong.homnayangi.domain.models.cusine.CuisineDish;
import com.camlong.homnayangi.domain.models.cusine.CuisineDishFilter;

import java.util.List;
import java.util.Optional;

public interface CuisineDishRepository {

  Optional<CuisineDish> findById(Long id);

  CuisineDish save(CuisineDish cuisineDish);

  List<CuisineDish> findAll();

  List<CuisineDish> findAllWithFilter(CuisineDishFilter filter);

  void delete(Long id);

}
