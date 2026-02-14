package com.camlong.homnayangi.service;

import com.camlong.homnayangi.dto.AccountRegisterRequest;
import com.camlong.homnayangi.dto.AuthResponse;

public interface AuthService {

  AuthResponse login(String username, String password);

  void logout(String refreshToken);

  AuthResponse refreshToken(String refreshToken);

  void register(AccountRegisterRequest registration);
}
