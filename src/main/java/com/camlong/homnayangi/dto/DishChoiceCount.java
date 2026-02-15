package com.camlong.homnayangi.dto;

import java.time.Instant;

public record DishChoiceCount(Long dishId, long choiceCount, Instant lastChosenTime) {
}
