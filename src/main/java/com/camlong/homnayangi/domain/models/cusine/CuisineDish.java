package com.camlong.homnayangi.domain.models.cusine;

import com.camlong.homnayangi.domain.models.DomainAuditor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
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
