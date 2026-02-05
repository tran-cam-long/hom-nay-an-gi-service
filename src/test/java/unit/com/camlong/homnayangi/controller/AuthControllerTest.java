package unit.com.camlong.homnayangi.controller;

import com.camlong.homnayangi.controller.AuthController;
import com.camlong.homnayangi.dto.AccountRegisterRequest;
import com.camlong.homnayangi.dto.AuthResponse;
import com.camlong.homnayangi.dto.LoginRequest;
import com.camlong.homnayangi.service.AuthService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @InjectMocks
  private AuthController authController;

  @Mock
  private AuthService authService;

  static final String SAMPLE_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlVzZXIiXSwiaWF0IjoxNzcwMjk2MzM4LCJleHAiOjE3NzAyOTk5Mzh9.MhSIn8D6TI4hn7sVK9zR1EaRBjKX93BQPPBryz0YP6o";
  static final String SAMPLE_REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc3MDI5NjMzOCwiZXhwIjoxNzcwMzMyMzM4fQ.xzm1uoxu_79RbqN8aVecJ--19lUdSJmmv3FFmxgP_7o";

  @Test
  void givenValidLoginRequest_whenLogin_thenSucceed() {
    final LoginRequest request = new LoginRequest("john_doe", "123456");
    final AuthResponse response = new AuthResponse(SAMPLE_ACCESS_TOKEN, SAMPLE_REFRESH_TOKEN);

    when(authService.login(request.username(), request.password())).thenReturn(response);

    final ResponseEntity<@NonNull AuthResponse> actualResponse = authController.login(request);

    verify(authService, times(1)).login(request.username(), request.password());
    assertNotNull(actualResponse.getBody());
    assertEquals(SAMPLE_ACCESS_TOKEN, actualResponse.getBody().token());
    assertEquals(SAMPLE_REFRESH_TOKEN, actualResponse.getBody().refreshToken());
  }

  @Test
  void givenIncorrectRequest_whenLogin_thenThrowBadCredential() {
    final LoginRequest request = new LoginRequest("john_doe", "123456");

    when(authService.login(request.username(), request.password())).thenThrow(new BadCredentialsException("Bad credentials"));

    final BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authController.login(request));

    assertEquals("Bad credentials", exception.getMessage());
  }

  @Test
  void givenNullUsername_whenLogin_thenValidationFailsAndReturnsBadRequest() throws Exception {
    final MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(authController)
        .setValidator(new LocalValidatorFactoryBean())
        .build();

    final String json = "{\"username\":null,\"password\":\"\"}";

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(status().isBadRequest());
  }

  @Test
  void givenValidRefreshToken_whenRefreshToken_thenSucceed() {
    final AuthResponse response = new AuthResponse(SAMPLE_ACCESS_TOKEN, SAMPLE_REFRESH_TOKEN);

    when(authService.refreshToken(SAMPLE_REFRESH_TOKEN)).thenReturn(response);

    final ResponseEntity<@NonNull AuthResponse> actualResponse = authController.refresh(SAMPLE_REFRESH_TOKEN);

    verify(authService, times(1)).refreshToken(SAMPLE_REFRESH_TOKEN);
    assertNotNull(actualResponse.getBody());
    assertEquals(SAMPLE_ACCESS_TOKEN, actualResponse.getBody().token());
    assertEquals(SAMPLE_REFRESH_TOKEN, actualResponse.getBody().refreshToken());
  }

  @Test
  void givenValidRegisterRequest_whenRegister_thenSucceed() {
    final String username = "john_doe";
    final String password = "123456";
    final AccountRegisterRequest request = new AccountRegisterRequest(username, password);

    final ResponseEntity<@NonNull Void> actualResponse = authController.register(request);

    verify(authService, times(1)).register(request);
    assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
  }

  @Test
  void givenNullUsername_whenRegister_thenValidationFailsAndReturnsBadRequest() throws Exception {
    final MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(authController)
        .setValidator(new LocalValidatorFactoryBean())
        .build();

    final String json = "{\"username\":null,\"password\":\"\"}";

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)).andExpect(status().isBadRequest());
  }

}