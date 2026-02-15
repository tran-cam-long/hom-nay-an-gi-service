package com.camlong.homnayangi.service.impl;

import com.camlong.homnayangi.config.exception.BusinessException;
import com.camlong.homnayangi.dto.DishChoiceCount;
import com.camlong.homnayangi.dto.DishChoiceRecommendation;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void recordChoice(String username, Long dishId) {
        final ApplicationUser user = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User was not found"));
        final CuisineDish dish = cuisineDishRepository.findById(dishId)
                .orElseThrow(() -> new BusinessException("Dish was not found"));
        recordChoice(user, dish);
    }

    @Override
    public DishChoiceRecommendation getRecommendations(String username) {
        final ApplicationUser user = applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User was not found"));
        final List<CuisineDish> allDishes = cuisineDishRepository.findAll();
        final Map<Long, Long> choiceCountByDishId = dishChoiceRepository.findChoiceCountByUserId(user.getId()).stream()
                .collect(Collectors.toMap(DishChoiceCount::dishId, DishChoiceCount::choiceCount));

        final List<DishWithCount> dishesWithCount = allDishes.stream()
                .map(dish -> new DishWithCount(dish, choiceCountByDishId.getOrDefault(dish.getId(), 0L)))
                .toList();

        final Comparator<DishWithCount> byMostChosen = Comparator.comparingLong(DishWithCount::count)
                .reversed()
                .thenComparing(item -> item.dish().getId());
        final Comparator<DishWithCount> byLeastChosen = Comparator.comparingLong(DishWithCount::count)
                .thenComparing(item -> item.dish().getId());

        final List<DishWithCount> mostChosen = dishesWithCount.stream()
                .sorted(byMostChosen)
                .toList();
        final List<DishWithCount> leastChosen = dishesWithCount.stream()
                .sorted(byLeastChosen)
                .toList();

        final List<CuisineDish> userFavorites = mostChosen.stream()
                .limit(10)
                .map(DishWithCount::dish)
                .toList();

        final List<DishWithCount> top15 = mostChosen.stream()
                .limit(15)
                .toList();
        final int leastOftenStartIndex = Math.max(0, top15.size() - 5);
        final List<CuisineDish> userLeastOftenInTop = top15.subList(leastOftenStartIndex, top15.size()).stream()
                .map(DishWithCount::dish)
                .toList();

        final List<CuisineDish> userDiscovery = getUserDiscovery(leastChosen);

        return new DishChoiceRecommendation(userFavorites, userDiscovery, userLeastOftenInTop);
    }

    private List<CuisineDish> getUserDiscovery(List<DishWithCount> leastChosen) {
        if (leastChosen.size() <= 10) {
            return leastChosen.stream()
                    .map(DishWithCount::dish)
                    .toList();
        }

        final long cutoffCount = leastChosen.get(9).count();
        final List<DishWithCount> belowCutoff = leastChosen.stream()
                .filter(item -> item.count() < cutoffCount)
                .toList();
        final int remainingSlot = 10 - belowCutoff.size();

        if (remainingSlot <= 0) {
            return belowCutoff.stream()
                    .limit(10)
                    .map(DishWithCount::dish)
                    .toList();
        }

        final List<DishWithCount> boundaryItems = new ArrayList<>(leastChosen.stream()
                .filter(item -> item.count() == cutoffCount)
                .toList());
        Collections.shuffle(boundaryItems);

        final List<CuisineDish> result = new ArrayList<>();
        result.addAll(belowCutoff.stream().map(DishWithCount::dish).toList());
        result.addAll(boundaryItems.stream().limit(remainingSlot).map(DishWithCount::dish).toList());
        return result;
    }

    private record DishWithCount(CuisineDish dish, long count) {
    }

}
