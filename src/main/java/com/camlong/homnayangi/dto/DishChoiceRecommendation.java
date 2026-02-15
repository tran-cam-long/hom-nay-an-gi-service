package com.camlong.homnayangi.dto;

import com.camlong.homnayangi.entity.CuisineDish;
import java.util.List;

public record DishChoiceRecommendation(
        List<CuisineDish> userFavorites,
        List<CuisineDish> userDiscovery,
        List<CuisineDish> userLeastOftenInTop
) {
}
