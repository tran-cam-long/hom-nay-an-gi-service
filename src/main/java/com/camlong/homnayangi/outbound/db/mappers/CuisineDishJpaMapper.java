package com.camlong.homnayangi.outbound.db.mappers;

import com.camlong.homnayangi.domain.models.cusine.CuisineDish;
import com.camlong.homnayangi.outbound.db.entities.CuisineDishEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true)
)
public interface CuisineDishJpaMapper {

  CuisineDish toCuisineDish(CuisineDishEntity entity);

  CuisineDishEntity toEntity(CuisineDish cuisineDish);
}
