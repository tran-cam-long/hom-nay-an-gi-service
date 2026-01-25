package com.camlong.homnayangi.outbound.db;

import com.camlong.homnayangi.outbound.db.entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {

}
