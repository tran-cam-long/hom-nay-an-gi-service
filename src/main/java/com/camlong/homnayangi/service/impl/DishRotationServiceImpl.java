package com.camlong.homnayangi.service.impl;

import com.camlong.homnayangi.config.exception.BusinessException;
import com.camlong.homnayangi.dto.DishChoiceCount;
import com.camlong.homnayangi.dto.DishChoiceRecommendation;
import com.camlong.homnayangi.dto.DishChoiceRecommendationItem;
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

import java.util.Comparator;
import java.util.HashSet;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        final Map<Long, DishChoiceCount> statsByDishId = dishChoiceRepository.findChoiceCountByUserId(user.getId()).stream()
                .collect(Collectors.toMap(DishChoiceCount::dishId, item -> item));

        final List<DishWithStats> dishesWithStats = allDishes.stream()
                .map(dish -> {
                    final DishChoiceCount stat = statsByDishId.get(dish.getId());
                    return stat == null
                            ? new DishWithStats(dish, 0L, null)
                            : new DishWithStats(dish, stat.choiceCount(), stat.lastChosenTime());
                })
                .toList();

        final Comparator<DishWithStats> byMostChosen = Comparator.comparingLong(DishWithStats::count)
                .reversed()
                .thenComparing(item -> item.dish().getId());
        final Comparator<DishWithStats> byLeastChosen = Comparator.comparingLong(DishWithStats::count)
                .thenComparing(item -> item.dish().getId());

        final List<DishWithStats> chosenDishes = dishesWithStats.stream()
                .filter(item -> item.count() > 0)
                .toList();
        final List<DishWithStats> unchosenDishes = dishesWithStats.stream()
                .filter(item -> item.count() == 0)
                .sorted(byLeastChosen)
                .toList();

        final List<DishWithStats> mostChosen = chosenDishes.stream()
                .sorted(byMostChosen)
                .toList();
        final List<DishWithStats> leastChosen = chosenDishes.stream()
                .sorted(byLeastChosen)
                .toList();

        final Set<Long> selectedDishIds = new HashSet<>();
        final List<DishChoiceRecommendationItem> userFavorites = mostChosen.stream()
                .limit(10)
                .filter(item -> selectedDishIds.add(item.dish().getId()))
                .map(this::toRecommendationItem)
                .toList();

        final List<DishWithStats> top15 = mostChosen.stream()
                .limit(15)
                .toList();
        final int leastOftenStartIndex = Math.max(0, top15.size() - 5);
        final List<DishChoiceRecommendationItem> userLeastOftenInTop = top15.subList(leastOftenStartIndex, top15.size()).stream()
                .filter(item -> selectedDishIds.add(item.dish().getId()))
                .map(this::toRecommendationItem)
                .toList();

        final List<DishWithStats> remainingForDiscovery = unchosenDishes.stream()
                .filter(item -> !selectedDishIds.contains(item.dish().getId()))
                .toList();
        final List<DishChoiceRecommendationItem> userDiscovery = remainingForDiscovery.stream()
                .map(this::toRecommendationItem)
                .toList();

        return new DishChoiceRecommendation(userFavorites, userDiscovery, userLeastOftenInTop);
    }

    private DishChoiceRecommendationItem toRecommendationItem(DishWithStats item) {
        return new DishChoiceRecommendationItem(item.dish(), item.count(), item.lastChosenTime());
    }

    private record DishWithStats(CuisineDish dish, long count, Instant lastChosenTime) {
    }

}
