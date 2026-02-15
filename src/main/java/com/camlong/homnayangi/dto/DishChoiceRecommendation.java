package com.camlong.homnayangi.dto;

import java.util.List;

public record DishChoiceRecommendation(
        List<DishChoiceRecommendationItem> userFavorites,
        List<DishChoiceRecommendationItem> userDiscovery,
        List<DishChoiceRecommendationItem> userLeastOftenInTop
) {
}
