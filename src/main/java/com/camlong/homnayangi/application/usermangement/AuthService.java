package com.camlong.homnayangi.application.usermangement;

import com.camlong.homnayangi.domain.models.ApplicationUser;
import com.camlong.homnayangi.domain.models.UserRegistration;

public interface AuthService {

  ApplicationUser login(String username, String password);

  ApplicationUser refreshToken(String refreshToken);

  void register(UserRegistration registration);
}
