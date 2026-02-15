package com.camlong.homnayangi.service;

import com.camlong.homnayangi.dto.DishChoiceRecommendation;
import com.camlong.homnayangi.entity.ApplicationUser;
import com.camlong.homnayangi.entity.CuisineDish;

public interface DishRotationService {
    void recordChoice(ApplicationUser currentUser, CuisineDish dish);

    void recordChoice(String username, Long dishId);

    DishChoiceRecommendation getRecommendations(String username);
}
