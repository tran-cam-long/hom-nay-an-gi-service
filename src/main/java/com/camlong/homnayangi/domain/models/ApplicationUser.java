package com.camlong.homnayangi.domain.models;

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
public class ApplicationUser extends DomainAuditor implements Serializable  {

    @Serial
    private static final long serialVersionUID = 3218120511901102544L;

    private String username;

    private String password;

    private String role;

    private String accessToken;
    private String refreshToken;
}
