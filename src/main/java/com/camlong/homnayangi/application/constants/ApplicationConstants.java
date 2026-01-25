package com.camlong.homnayangi.application.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ApplicationConstants {
  public static final String SYSTEM = "System";

  @Getter
  @AllArgsConstructor
  public enum Role {
    ROLE_USER("User");

    private final String value;
  }
}
