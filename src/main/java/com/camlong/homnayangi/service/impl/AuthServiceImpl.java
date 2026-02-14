package com.camlong.homnayangi.service.impl;

import com.camlong.homnayangi.config.exception.BusinessException;
import com.camlong.homnayangi.dto.AuthResponse;
import com.camlong.homnayangi.service.AuthService;
import com.camlong.homnayangi.config.auth.JwtUtils;
import com.camlong.homnayangi.entity.ApplicationUser;
import com.camlong.homnayangi.entity.RefreshToken;
import com.camlong.homnayangi.dto.AccountRegisterRequest;
import com.camlong.homnayangi.repository.ApplicationUserRepository;
import com.camlong.homnayangi.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.camlong.homnayangi.constant.ApplicationConstants.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  @Value("${jwt.refresh-expiration}")
  private long refreshExpiration;

  private final ApplicationUserRepository applicationUserRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthResponse login(String username, String password) {
    log.info("[Login]: Received login request for username {}", username);
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            username, password)
    );

    final ApplicationUser appUser = applicationUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user found for this username"));

    final String accessToken = jwtUtils.generateToken(username, List.of(appUser.getRole()));
    final String refreshToken = jwtUtils.generateRefreshToken(username);

    createRefreshToken(username, refreshToken);
    log.info("[Login]: Username {} has logged in.", username);

    final Long userId = appUser.getId();
    return new AuthResponse(userId, username, accessToken, refreshToken);
  }

  @Override
  @Transactional
  public void logout(String refreshToken) {
    final RefreshToken token = verifyRefreshToken(refreshToken);

    try {
      refreshTokenRepository.deleteByToken(refreshToken);
    } catch (Exception e) {
      log.error("Error: {}", e.getMessage());
    }

    log.info("[Logout] User logged out: {}", token.getUsername());
  }

  @Override
  public AuthResponse refreshToken(String refreshToken) {
    final RefreshToken token = verifyRefreshToken(refreshToken);
    final ApplicationUser appUser = applicationUserRepository.findByUsername(token.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No user found for this token"));

    final String newAccessToken = jwtUtils.generateToken(appUser.getUsername(), List.of(appUser.getRole()));

    return new AuthResponse(null, null, newAccessToken, refreshToken);
  }

  @Override
  public void register(AccountRegisterRequest registration) {
    if (applicationUserRepository.findByUsername(registration.username()).isPresent()) {
      throw new BusinessException("Existing username");
    }

    final ApplicationUser user = ApplicationUser.builder()
        .username(registration.username())
        .password(passwordEncoder.encode(registration.password()))
        .role(ROLE_USER).build();
    user.initToBeCreatedEntity();

    applicationUserRepository.save(user);
  }

  private void createRefreshToken(String username, String token) {
    final RefreshToken refreshToken = RefreshToken.builder()
        .username(username).token(token).expiryTime(Instant.now().plusMillis(refreshExpiration)).build();

    refreshTokenRepository.save(refreshToken);
  }

  private RefreshToken verifyRefreshToken(String token) {
    final RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
        .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    if (refreshToken.getExpiryTime().isBefore(Instant.now())) {
      refreshTokenRepository.deleteByUsername(refreshToken.getUsername());
      throw new RuntimeException("Refresh token expired");
    }

    return refreshToken;
  }
}
