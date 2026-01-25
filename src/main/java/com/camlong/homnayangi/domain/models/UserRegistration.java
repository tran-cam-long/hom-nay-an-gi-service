package com.camlong.homnayangi.domain.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class UserRegistration implements Serializable {

  @Serial
  private static final long serialVersionUID = -8102264012057683057L;

  private String username;
  private String password;
}
