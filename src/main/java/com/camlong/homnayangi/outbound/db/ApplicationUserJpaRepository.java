package com.camlong.homnayangi.outbound.db;

import com.camlong.homnayangi.outbound.db.entities.ApplicationUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationUserJpaRepository extends JpaRepository<ApplicationUserEntity, Long> {

    Optional<ApplicationUserEntity> findByUsername(String username);
}
