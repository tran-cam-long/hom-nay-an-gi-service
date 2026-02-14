package com.camlong.homnayangi.dto;

import jakarta.validation.constraints.NotNull;

public record DishChoiceSubmitRequest(@NotNull Long dishId) {
}
