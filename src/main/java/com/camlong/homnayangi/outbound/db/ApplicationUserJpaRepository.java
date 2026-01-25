package com.camlong.homnayangi.outbound.db;

import com.camlong.homnayangi.outbound.db.entities.ApplicationUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserJpaRepository extends JpaRepository<ApplicationUserEntity, Long> {

    Optional<ApplicationUserEntity> findByUsername(String username);
}
