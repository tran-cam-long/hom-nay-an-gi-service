package com.camlong.homnayangi.application.domain.models;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApplicationRefreshToken {

  private Long id;
  private String token;
  private String username;
  private Instant expiryTime;
}
