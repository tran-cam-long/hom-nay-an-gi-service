package com.camlong.homnayangi.dto;

import jakarta.validation.constraints.NotBlank;

public record GetUserInfoResponse(@NotBlank String userId,
                                  @NotBlank String username) { }
