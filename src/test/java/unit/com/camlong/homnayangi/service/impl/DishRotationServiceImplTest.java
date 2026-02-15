package unit.com.camlong.homnayangi.service.impl;

import com.camlong.homnayangi.dto.DishChoiceCount;
import com.camlong.homnayangi.dto.DishChoiceRecommendation;
import com.camlong.homnayangi.dto.DishChoiceRecommendationItem;
import com.camlong.homnayangi.entity.ApplicationUser;
import com.camlong.homnayangi.entity.CuisineDish;
import com.camlong.homnayangi.repository.ApplicationUserRepository;
import com.camlong.homnayangi.repository.CuisineDishRepository;
import com.camlong.homnayangi.repository.DishChoiceRepository;
import com.camlong.homnayangi.service.impl.DishRotationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishRotationServiceImplTest {

    @InjectMocks
    private DishRotationServiceImpl dishRotationService;

    @Mock
    private DishChoiceRepository dishChoiceRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private CuisineDishRepository cuisineDishRepository;

    @Test
    void getRecommendations_shouldReturnTop10Favorites() {
        final String username = "user-a";
        final ApplicationUser user = buildUser(1L, username);
        final List<CuisineDish> allDishes = buildDishes(12);
        final Map<Long, Long> counts = Map.ofEntries(
                Map.entry(1L, 30L),
                Map.entry(2L, 25L),
                Map.entry(3L, 20L),
                Map.entry(4L, 18L),
                Map.entry(5L, 17L),
                Map.entry(6L, 15L),
                Map.entry(7L, 14L),
                Map.entry(8L, 12L),
                Map.entry(9L, 10L),
                Map.entry(10L, 9L),
                Map.entry(11L, 8L),
                Map.entry(12L, 7L)
        );

        mockCommonDependencies(user, allDishes, toChoiceCounts(counts));

        final DishChoiceRecommendation result = dishRotationService.getRecommendations(username);

        assertEquals(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L), extractDishIds(result.userFavorites()));
        assertEquals(30L, result.userFavorites().get(0).timesChosen());
        assertNotNull(result.userFavorites().get(0).lastChosenTime());
    }

    @Test
    void getRecommendations_shouldReturnUnchosenDishesInDiscoveryOnly() {
        final String username = "user-b";
        final ApplicationUser user = buildUser(2L, username);
        final List<CuisineDish> allDishes = buildDishes(30);
        final Map<Long, Long> counts = Map.ofEntries(
                Map.entry(1L, 0L),
                Map.entry(2L, 0L),
                Map.entry(3L, 0L),
                Map.entry(4L, 1L),
                Map.entry(5L, 1L),
                Map.entry(6L, 1L),
                Map.entry(7L, 1L),
                Map.entry(8L, 1L),
                Map.entry(9L, 1L),
                Map.entry(10L, 1L),
                Map.entry(11L, 1L),
                Map.entry(12L, 1L),
                Map.entry(13L, 2L),
                Map.entry(14L, 3L),
                Map.entry(15L, 4L),
                Map.entry(16L, 5L),
                Map.entry(17L, 6L),
                Map.entry(18L, 7L),
                Map.entry(19L, 8L),
                Map.entry(20L, 9L),
                Map.entry(21L, 10L),
                Map.entry(22L, 11L),
                Map.entry(23L, 12L),
                Map.entry(24L, 13L),
                Map.entry(25L, 14L),
                Map.entry(26L, 15L),
                Map.entry(27L, 16L),
                Map.entry(28L, 17L),
                Map.entry(29L, 18L),
                Map.entry(30L, 19L)
        );

        mockCommonDependencies(user, allDishes, toChoiceCounts(counts));

        final DishChoiceRecommendation result = dishRotationService.getRecommendations(username);
        final List<Long> discoveryDishIds = extractDishIds(result.userDiscovery());
        final List<Long> zeroCountIds = discoveryDishIds.stream()
                .filter(id -> counts.get(id) == 0L)
                .toList();
        final List<Long> oneCountIds = discoveryDishIds.stream()
                .filter(id -> counts.get(id) == 1L)
                .toList();

        assertEquals(3, discoveryDishIds.size());
        assertEquals(3, zeroCountIds.size());
        assertEquals(0, oneCountIds.size());
        assertTrue(discoveryDishIds.containsAll(List.of(1L, 2L, 3L)));
        assertTrue(extractDishIds(result.userFavorites()).stream().noneMatch(discoveryDishIds::contains));
        assertTrue(extractDishIds(result.userLeastOftenInTop()).stream().noneMatch(discoveryDishIds::contains));
    }

    @Test
    void getRecommendations_shouldReturnLast5FromTop15() {
        final String username = "user-c";
        final ApplicationUser user = buildUser(3L, username);
        final List<CuisineDish> allDishes = buildDishes(20);
        final List<DishChoiceCount> counts = LongStream.rangeClosed(1L, 20L)
                .mapToObj(id -> new DishChoiceCount(id, 200L - id, Instant.parse("2026-01-01T00:00:00Z").plusSeconds(id)))
                .toList();

        mockCommonDependencies(user, allDishes, counts);

        final DishChoiceRecommendation result = dishRotationService.getRecommendations(username);

        assertEquals(List.of(11L, 12L, 13L, 14L, 15L), extractDishIds(result.userLeastOftenInTop()));
    }

    @Test
    void getRecommendations_shouldHandleFewerThanThresholds() {
        final String username = "user-d";
        final ApplicationUser user = buildUser(4L, username);
        final List<CuisineDish> allDishes = buildDishes(8);

        mockCommonDependencies(user, allDishes, List.of());

        final DishChoiceRecommendation result = dishRotationService.getRecommendations(username);

        assertEquals(0, result.userFavorites().size());
        assertEquals(8, result.userDiscovery().size());
        assertEquals(0, result.userLeastOftenInTop().size());
        assertEquals(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L), extractDishIds(result.userDiscovery()));
    }

    private void mockCommonDependencies(ApplicationUser user, List<CuisineDish> allDishes, List<DishChoiceCount> choiceCounts) {
        when(applicationUserRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(cuisineDishRepository.findAll()).thenReturn(allDishes);
        when(dishChoiceRepository.findChoiceCountByUserId(user.getId())).thenReturn(choiceCounts);
    }

    private ApplicationUser buildUser(Long userId, String username) {
        return ApplicationUser.builder()
                .id(userId)
                .username(username)
                .build();
    }

    private List<CuisineDish> buildDishes(int total) {
        final List<CuisineDish> dishes = new ArrayList<>();
        for (long id = 1; id <= total; id++) {
            dishes.add(CuisineDish.builder()
                    .id(id)
                    .name("Dish " + id)
                    .type("type-" + id)
                    .culture("culture-" + id)
                    .imageUrl("https://img/" + id)
                    .build());
        }
        return dishes;
    }

    private List<DishChoiceCount> toChoiceCounts(Map<Long, Long> countByDishId) {
        return countByDishId.entrySet().stream()
                .map(entry -> new DishChoiceCount(entry.getKey(), entry.getValue(), Instant.now()))
                .toList();
    }

    private List<Long> extractDishIds(List<DishChoiceRecommendationItem> dishes) {
        return dishes.stream().map(item -> item.dish().getId()).collect(Collectors.toList());
    }
}
