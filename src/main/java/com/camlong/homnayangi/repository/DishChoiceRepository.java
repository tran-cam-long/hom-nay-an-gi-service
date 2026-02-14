package com.camlong.homnayangi.repository;

import com.camlong.homnayangi.entity.DishChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishChoiceRepository extends JpaRepository<DishChoice, Long> {
}
