package com.camlong.homnayangi.controller;


import com.camlong.homnayangi.dto.GetUserInfoResponse;
import com.camlong.homnayangi.dto.LogoutRequest;
import com.camlong.homnayangi.service.AuthService;
import com.camlong.homnayangi.dto.AccountRegisterRequest;
import com.camlong.homnayangi.dto.LoginRequest;
import com.camlong.homnayangi.dto.AuthResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<@NonNull AuthResponse> login(@RequestBody @Valid LoginRequest request) {
    final AuthResponse authResponse = authService.login(request.username(), request.password());
    return ResponseEntity.ok(authResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequest request) {
    authService.logout(request.refreshToken());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<GetUserInfoResponse> getUserInfo(Authentication authentication) {
    return ResponseEntity.ok(authService.getUserInfoResponse(authentication.getName()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<@NonNull AuthResponse> refresh(@RequestParam String refreshToken) {
    final AuthResponse authResponse = authService.refreshToken(refreshToken);
    return ResponseEntity.ok(authResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<@NonNull Void> register(@RequestBody @Valid AccountRegisterRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
