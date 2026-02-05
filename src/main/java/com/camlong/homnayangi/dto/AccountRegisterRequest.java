package com.camlong.homnayangi.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountRegisterRequest(@NotBlank String username, @NotBlank String password){
}
