package com.camlong.homnayangi.outbound.db;

import com.camlong.homnayangi.outbound.db.entities.CuisineDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineDishJpaRepository extends JpaRepository<CuisineDishEntity, Long> {
}
