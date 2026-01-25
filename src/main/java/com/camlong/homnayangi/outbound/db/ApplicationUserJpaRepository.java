package com.camlong.homnayangi.outbound.db;

import com.camlong.homnayangi.outbound.db.entities.ApplicationUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserJpaRepository extends JpaRepository<ApplicationUserEntity, Long> {
}
