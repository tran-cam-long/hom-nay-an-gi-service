package com.camlong.homnayangi.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ApplicationUser extends DomainAuditor implements Serializable  {

    @Serial
    private static final long serialVersionUID = 3218120511901102544L;

    private String username;

    private String password;

    private String role;

    private String accessToken;
    private String refreshToken;
}
