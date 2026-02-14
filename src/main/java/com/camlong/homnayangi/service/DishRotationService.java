package com.camlong.homnayangi.service;

import com.camlong.homnayangi.entity.ApplicationUser;
import com.camlong.homnayangi.entity.CuisineDish;

public interface DishRotationService {
    void recordChoice(ApplicationUser currentUser, CuisineDish dish);

    void recordChoice(Long userId, Long dishId);
}
