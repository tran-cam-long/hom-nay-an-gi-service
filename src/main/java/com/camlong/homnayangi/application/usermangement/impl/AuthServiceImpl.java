package com.camlong.homnayangi.application.usermangement.impl;

import com.camlong.homnayangi.application.domain.models.ApplicationRefreshToken;
import com.camlong.homnayangi.application.domain.models.ApplicationUser;
import com.camlong.homnayangi.application.domain.models.UserRegistration;
import com.camlong.homnayangi.application.usermangement.ApplicationUserRepository;
import com.camlong.homnayangi.application.usermangement.AuthService;
import com.camlong.homnayangi.application.usermangement.RefreshTokenRepository;
import com.camlong.homnayangi.application.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.camlong.homnayangi.application.constants.ApplicationConstants.Role.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  @Value("${jwt.refresh-expiration}")
  private long refreshExpiration;

  private final ApplicationUserRepository userRepository;
  private final RefreshTokenRepository repository;

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ApplicationUser login(String username, String password) {
    log.info("[Login]: Received login request for username {}", username);
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            username, password)
    );

    final ApplicationUser appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user found for this username"));

    final String accessToken = jwtUtils.generateToken(username, List.of(appUser.getRole()));
    final String refreshToken = jwtUtils.generateRefreshToken(username);
    appUser.setAccessToken(accessToken);
    appUser.setRefreshToken(refreshToken);

    log.info("[Login]: Username {} has logged in.", username);

    return appUser;
  }

  @Override
  public ApplicationUser refreshToken(String refreshToken) {
    final ApplicationRefreshToken token = verifyRefreshToken(refreshToken);
    final ApplicationUser appUser = userRepository.findByUsername(token.getUsername()).orElseThrow(() -> new UsernameNotFoundException("No user found for this token"));

    final String newAccessToken = jwtUtils.generateToken(appUser.getUsername(), List.of(appUser.getRole()));
    appUser.setAccessToken(newAccessToken);

    return appUser;
  }

  @Override
  public void register(UserRegistration registration) {
    if (userRepository.findByUsername(registration.getUsername()).isPresent()) {
      throw new RuntimeException("Existing username");
    }

    final ApplicationUser user = ApplicationUser.builder()
        .username(registration.getUsername())
        .password(passwordEncoder.encode(registration.getPassword()))
        .role(ROLE_USER.getValue()).build();
    user.initCreatedDomainModel();

    userRepository.save(user);
  }

  private ApplicationRefreshToken createRefreshToken(String username, String token) {
    final ApplicationRefreshToken refreshToken = ApplicationRefreshToken.builder()
        .username(username).token(token).expiryTime(Instant.now().plusMillis(refreshExpiration)).build();

    return repository.save(refreshToken);
  }

  private ApplicationRefreshToken verifyRefreshToken(String token) {
    final ApplicationRefreshToken refreshToken = repository.findByToken(token)
        .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    if (refreshToken.getExpiryTime().isBefore(Instant.now())) {
      repository.deleteByUsername(refreshToken.getUsername());
      throw new RuntimeException("Refresh token expired");
    }

    return refreshToken;
  }
}
