package com.camlong.homnayangi.application.usermangement;

import com.camlong.homnayangi.application.domain.models.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserRepository {
    Optional<ApplicationUser> findById(Long id);

    boolean save(ApplicationUser applicationUser);
}
