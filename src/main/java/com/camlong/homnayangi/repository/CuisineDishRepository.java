package com.camlong.homnayangi.repository;

import com.camlong.homnayangi.entity.CuisineDish;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineDishRepository extends JpaRepository<@NonNull CuisineDish, @NonNull Long> {
}
