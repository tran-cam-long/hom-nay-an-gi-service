package com.camlong.homnayangi.application.domain.models;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class GoogleSource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;
}
