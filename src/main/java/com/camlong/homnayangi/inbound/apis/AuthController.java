package com.camlong.homnayangi.inbound.apis;

import com.camlong.homnayangi.domain.models.ApplicationUser;
import com.camlong.homnayangi.domain.models.UserRegistration;
import com.camlong.homnayangi.application.usermangement.AuthService;
import com.camlong.homnayangi.inbound.models.requests.AccountRegisterRequest;
import com.camlong.homnayangi.inbound.models.requests.LoginRequest;
import com.camlong.homnayangi.inbound.models.responses.AuthResponse;
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
    final ApplicationUser appUser = authService.login(request.username(), request.password());

    return ResponseEntity.ok(new AuthResponse(appUser.getAccessToken(), appUser.getRefreshToken()));
  }

  @PostMapping("/refresh")
  public ResponseEntity<@NonNull AuthResponse> refresh(@RequestParam String refreshToken) {
    final ApplicationUser appUser = authService.refreshToken(refreshToken);

    return ResponseEntity.ok(new AuthResponse(appUser.getAccessToken(), refreshToken));
  }

  @PostMapping("/register")
  public ResponseEntity<@NonNull Void> register(@RequestBody @Valid AccountRegisterRequest request) {
    authService.register(UserRegistration.builder()
        .username(request.username()).password(request.password()).build());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
