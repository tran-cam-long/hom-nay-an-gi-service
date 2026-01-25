package com.camlong.homnayangi.inbound.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@NotEmpty
public record CreateCuisineDishRequest(@NotBlank String name, @NotBlank String type, String culture, String imageUrl, String searchKeyword) {
}
