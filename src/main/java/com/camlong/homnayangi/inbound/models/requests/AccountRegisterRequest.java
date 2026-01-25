package com.camlong.homnayangi.inbound.models.requests;

import jakarta.validation.constraints.NotBlank;

public record AccountRegisterRequest(@NotBlank String username, @NotBlank String password){
}
