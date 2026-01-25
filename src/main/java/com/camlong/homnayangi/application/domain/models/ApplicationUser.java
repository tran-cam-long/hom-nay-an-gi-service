package com.camlong.homnayangi.application.domain.models;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ApplicationUser implements Serializable  {

    @Serial
    private static final long serialVersionUID = 3218120511901102544L;

    private String username;

    private String password;

    private String role;
}
