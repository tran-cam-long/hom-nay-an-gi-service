package com.camlong.homnayangi.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

import static com.camlong.homnayangi.application.constants.ApplicationConstants.SYSTEM;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DomainAuditor implements Serializable {

  @Serial
  private static final long serialVersionUID = -2442240160382109686L;

  protected Instant createdAt;
  protected String createdBy;
  protected Instant updatedAt;
  protected String updatedBy;

  public void initCreatedDomainModel() {
    this.createdAt = Instant.now();
    this.createdBy = SYSTEM;
    this.updatedAt = Instant.now();
    this.updatedBy = SYSTEM;
  }
}
