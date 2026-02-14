package com.camlong.homnayangi.service.impl;

import com.camlong.homnayangi.config.exception.BusinessException;
import com.camlong.homnayangi.entity.ApplicationUser;
import com.camlong.homnayangi.entity.CuisineDish;
import com.camlong.homnayangi.entity.DishChoice;
import com.camlong.homnayangi.repository.ApplicationUserRepository;
import com.camlong.homnayangi.repository.CuisineDishRepository;
import com.camlong.homnayangi.repository.DishChoiceRepository;
import com.camlong.homnayangi.service.DishRotationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishRotationServiceImpl implements DishRotationService {
    private final DishChoiceRepository dishChoiceRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final CuisineDishRepository cuisineDishRepository;

    @Override
    public void recordChoice(ApplicationUser currentUser, CuisineDish dish) {
        final DishChoice choice = DishChoice.builder().dish(dish).user(currentUser).build();
        choice.initToBeCreatedEntity();
        dishChoiceRepository.save(choice);
    }

    @Override
    public void recordChoice(Long userId, Long dishId) {
        final ApplicationUser user = applicationUserRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User was not found"));
        final CuisineDish dish = cuisineDishRepository.findById(dishId)
                .orElseThrow(() -> new BusinessException("Dish was not found"));
        recordChoice(user, dish);
    }


}
