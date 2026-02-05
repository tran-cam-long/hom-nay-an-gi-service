package com.camlong.homnayangi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Data
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken implements Serializable {

  @Serial
  private static final long serialVersionUID = -6943139483927592304L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private Instant expiryTime;
}
