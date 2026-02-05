package com.camlong.homnayangi.repository;

import com.camlong.homnayangi.entity.ApplicationUser;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ApplicationUserRepository extends JpaRepository<@NonNull ApplicationUser, @NonNull Long> {

  Optional<ApplicationUser> findByUsername(String username);
}
