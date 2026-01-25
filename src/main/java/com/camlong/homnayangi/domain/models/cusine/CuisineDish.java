package com.camlong.homnayangi.domain.models.cusine;

import com.camlong.homnayangi.domain.models.DomainAuditor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class CuisineDish extends DomainAuditor implements Serializable {

  @Serial
  private static final long serialVersionUID = 122231598554927029L;

  private Long id;
  private String name;
  private String searchKeyword;
  private String type;
  private String culture;
  private String imageUrl;

}
