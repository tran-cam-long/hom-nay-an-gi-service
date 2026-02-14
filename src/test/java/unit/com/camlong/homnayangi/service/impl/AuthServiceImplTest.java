package unit.com.camlong.homnayangi.service.impl;

import com.camlong.homnayangi.config.auth.JwtUtils;
import com.camlong.homnayangi.dto.AccountRegisterRequest;
import com.camlong.homnayangi.dto.AuthResponse;
import com.camlong.homnayangi.entity.ApplicationUser;
import com.camlong.homnayangi.entity.RefreshToken;
import com.camlong.homnayangi.repository.ApplicationUserRepository;
import com.camlong.homnayangi.repository.RefreshTokenRepository;
import com.camlong.homnayangi.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.camlong.homnayangi.constant.ApplicationConstants.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private ApplicationUserRepository applicationUserRepository;
  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private JwtUtils jwtUtils;
  @Mock
  private RefreshTokenRepository refreshTokenRepository;
  @Mock
  private PasswordEncoder passwordEncoder;

  static final String SAMPLE_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlVzZXIiXSwiaWF0IjoxNzcwMjk2MzM4LCJleHAiOjE3NzAyOTk5Mzh9.MhSIn8D6TI4hn7sVK9zR1EaRBjKX93BQPPBryz0YP6o";
  static final String SAMPLE_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc3MDI5NjMzOCwiZXhwIjoxNzcwMzMyMzM4fQ.xzm1uoxu_79RbqN8aVecJ--19lUdSJmmv3FFmxgP_7o";
  static final String SAMPLE_ENCODED_PASSWORD = "$2a$10$DJcj.euKoLPn8/YrB9NdveNMnf7dW3lTvBimDun22j0YxQ/4xMnje";

  @Test
  void givenExistingUsernameAndCorrectPassword_whenLogin_thenSucceed() {
    final String username = "john_doe";
    final String password = "123456";
    final ApplicationUser user = ApplicationUser.builder().role("USER").build();

    when(applicationUserRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(jwtUtils.generateToken(username, List.of(user.getRole()))).thenReturn(SAMPLE_ACCESS_TOKEN);
    when(jwtUtils.generateRefreshToken(username)).thenReturn(SAMPLE_REFRESH_TOKEN);

    final AuthResponse response = authService.login(username, password);
    final ArgumentCaptor<RefreshToken> refreshTokenArgumentCaptor = ArgumentCaptor.forClass(RefreshToken.class);

    verify(refreshTokenRepository, times(1)).save(refreshTokenArgumentCaptor.capture());
    verify(applicationUserRepository, times(1)).findByUsername(username);

    final RefreshToken refreshToken = refreshTokenArgumentCaptor.getValue();
    assertEquals(SAMPLE_REFRESH_TOKEN, refreshToken.getToken());
    assertFalse(refreshToken.getExpiryTime().isAfter(Instant.now().plusSeconds(5)), "expiry should be within tolerance");
    assertEquals(SAMPLE_ACCESS_TOKEN, response.token());
    assertEquals(SAMPLE_REFRESH_TOKEN, response.refreshToken());
  }

  @Test
  void givenExistingUsernameAndWrongPassword_whenLogin_thenThrowAuthenticationException() {
    final String username = "john_doe";
    final String password = "123456";
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

    when(authenticationManager.authenticate(authenticationToken)).thenThrow(new BadCredentialsException("Bad credentials"));

    assertThrows(BadCredentialsException.class, () -> authService.login(username, password));
  }

  @Test
  void givenExistingUsername_whenRegister_thenThrowBusinessException() {
    final String username = "john_doe1";
    final String password = "123456";
    final ApplicationUser user = ApplicationUser.builder().username(username).build();
    final AccountRegisterRequest request = new AccountRegisterRequest(username, password);

    when(applicationUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

    final RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.register(request));

    assertEquals("Existing username", exception.getMessage());
  }

  @Test
  void givenNonexistingUsername_whenRegister_thenSucceed() {
    final String username = "john_doe1";
    final String password = "123456";
    final AccountRegisterRequest request = new AccountRegisterRequest(username, password);

    when(applicationUserRepository.findByUsername(username)).thenReturn(Optional.empty());
    when(passwordEncoder.encode(password)).thenReturn(SAMPLE_ENCODED_PASSWORD);

    final ArgumentCaptor<ApplicationUser> argumentCaptor = ArgumentCaptor.forClass(ApplicationUser.class);

    authService.register(request);

    verify(applicationUserRepository, times(1)).save(argumentCaptor.capture());
    verify(applicationUserRepository, times(1)).findByUsername(username);

    final ApplicationUser saved = argumentCaptor.getValue();
    assertEquals(username, saved.getUsername());
    assertEquals(SAMPLE_ENCODED_PASSWORD, saved.getPassword());
    assertEquals(ROLE_USER, saved.getRole());
  }

  @Test
  void givenCorrectRefreshToken_whenRefreshToken_thenReturnNewAccessToken() {
    final String refreshToken = SAMPLE_REFRESH_TOKEN;
    final String username = "john_doe";
    final RefreshToken refreshTokenEntity = RefreshToken.builder()
        .username(username).token(refreshToken).expiryTime(Instant.now().plusSeconds(7200)).build();
    final ApplicationUser user = ApplicationUser.builder().username(username).role(ROLE_USER).build();

    when(refreshTokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(refreshTokenEntity));
    when(applicationUserRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(jwtUtils.generateToken(username, List.of(user.getRole()))).thenReturn(SAMPLE_ACCESS_TOKEN);

    final AuthResponse result = authService.refreshToken(refreshToken);

    verify(applicationUserRepository, times(1)).findByUsername(username);
    verify(refreshTokenRepository, times(1)).findByToken(refreshToken);
    assertEquals(SAMPLE_REFRESH_TOKEN, result.refreshToken());
    assertEquals(SAMPLE_ACCESS_TOKEN, result.token());
  }

  @Test
  void givenCorrectRefreshTokenHasExpired_whenRefreshToken_thenThrowRuntimeException() {
    final String refreshToken = SAMPLE_REFRESH_TOKEN;
    final String username = "john_doe";
    final RefreshToken refreshTokenEntity = RefreshToken.builder()
        .username(username).token(refreshToken).expiryTime(Instant.now().minusSeconds(7200)).build();
    ApplicationUser.builder().username(username).role(ROLE_USER).build();

    when(refreshTokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(refreshTokenEntity));

    final RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.refreshToken(refreshToken));

    assertEquals("Refresh token expired", exception.getMessage());
  }
}