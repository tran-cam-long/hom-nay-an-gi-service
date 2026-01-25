package com.camlong.homnayangi.application.usermangement;

import com.camlong.homnayangi.application.domain.models.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserRepository {
    Optional<ApplicationUser> findById(Long id);

    Optional<ApplicationUser> findByUsername(String username);

    boolean save(ApplicationUser applicationUser);
}
