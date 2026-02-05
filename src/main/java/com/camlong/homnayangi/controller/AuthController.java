package com.camlong.homnayangi.controller;


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
