package com.camlong.homnayangi.service;

import com.camlong.homnayangi.dto.AccountRegisterRequest;
import com.camlong.homnayangi.dto.AuthResponse;
import com.camlong.homnayangi.dto.GetUserInfoResponse;

public interface AuthService {

  AuthResponse login(String username, String password);

  void logout(String refreshToken);

  GetUserInfoResponse getUserInfoResponse(String username);

  AuthResponse refreshToken(String refreshToken);

  void register(AccountRegisterRequest registration);
}
