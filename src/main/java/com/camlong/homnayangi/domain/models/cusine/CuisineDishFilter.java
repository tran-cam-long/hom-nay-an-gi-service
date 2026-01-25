package com.camlong.homnayangi.domain.models.cusine;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CuisineDishFilter implements Serializable {

  @Serial
  private static final long serialVersionUID = -8934319947104327857L;

  private List<String> types;
  private List<String> names;
  private List<String> cultures;
}
