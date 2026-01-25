package com.camlong.homnayangi.application.domain.models;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

import static com.camlong.homnayangi.application.constants.ApplicationConstants.SYSTEM;

@Data
@MappedSuperclass
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
