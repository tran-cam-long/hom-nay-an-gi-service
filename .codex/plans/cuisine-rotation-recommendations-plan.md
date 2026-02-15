# Plan: `GET /api/cuisine/rotation/recommendations`

## Status
Completed on February 15, 2026.

## Final Goal
Provide personalized recommendation buckets from `DishChoice` history with metadata per dish:
- `userFavorites`: top 10 most chosen dishes (`timesChosen > 0`).
- `userLeastOftenInTop`: last 5 dishes from top 15 most chosen (`timesChosen > 0`).
- `userDiscovery`: dishes never chosen (`timesChosen == 0`).

Additional constraints:
- Dishes must be unique across all response lists.
- Priority for keeping duplicates is:
  1. `userFavorites`
  2. `userLeastOftenInTop`
  3. `userDiscovery`

`DishChoice` remains the source of user choice history (`src/main/java/com/camlong/homnayangi/entity/DishChoice.java`).

## API Contract (Implemented)
- Method: `GET`
- Path: `/api/cuisine/rotation/recommendations`
- Auth: `@PreAuthorize("hasAnyRole('Admin', 'User')")`
- Response DTO: `DishChoiceRecommendation`

Current response shape:

```json
{
  "userFavorites": [
    {
      "dish": { "id": 1, "name": "...", "type": "...", "culture": "...", "imageUrl": "..." },
      "timesChosen": 12,
      "lastChosenTime": "2026-02-14T12:34:56Z"
    }
  ],
  "userDiscovery": [
    {
      "dish": { "id": 2, "name": "...", "type": "...", "culture": "...", "imageUrl": "..." },
      "timesChosen": 0,
      "lastChosenTime": null
    }
  ],
  "userLeastOftenInTop": [
    {
      "dish": { "id": 3, "name": "...", "type": "...", "culture": "...", "imageUrl": "..." },
      "timesChosen": 4,
      "lastChosenTime": "2026-02-10T09:00:00Z"
    }
  ]
}
```

## What Was Implemented

### 1) DTO changes
- Updated `src/main/java/com/camlong/homnayangi/dto/DishChoiceRecommendation.java`
  - Now stores 3 lists of `DishChoiceRecommendationItem`.
- Added `src/main/java/com/camlong/homnayangi/dto/DishChoiceRecommendationItem.java`
  - Fields: `CuisineDish dish`, `long timesChosen`, `Instant lastChosenTime`.
- Updated `src/main/java/com/camlong/homnayangi/dto/DishChoiceCount.java`
  - Fields: `Long dishId`, `long choiceCount`, `Instant lastChosenTime`.

### 2) Repository aggregation
- Updated `src/main/java/com/camlong/homnayangi/repository/DishChoiceRepository.java`
- Implemented:

```java
@Query("""
    select new com.camlong.homnayangi.dto.DishChoiceCount(dc.dish.id, count(dc.id), max(dc.createdAt))
    from DishChoice dc
    where dc.user.id = :userId
    group by dc.dish.id
""")
List<DishChoiceCount> findChoiceCountByUserId(@Param("userId") Long userId);
```

### 3) Service interface
- Updated `src/main/java/com/camlong/homnayangi/service/DishRotationService.java`
- Added:

```java
DishChoiceRecommendation getRecommendations(String username);
```

### 4) Service implementation
- Updated `src/main/java/com/camlong/homnayangi/service/impl/DishRotationServiceImpl.java`
- Implemented recommendation pipeline:
  - Load user and all dishes.
  - Load aggregated stats map from repository.
  - Build in-memory dish stats (`count`, `lastChosenTime`) with default `(0, null)`.
  - Partition dishes:
    - `chosen`: `count > 0`
    - `unchosen`: `count == 0`
  - Build `userFavorites` from chosen dishes sorted by `count DESC`, tie by `dish.id ASC`, limit 10.
  - Build `userLeastOftenInTop` from ranks 11-15 of the chosen top 15 (same sorting).
  - Build `userDiscovery` from unchosen dishes.
  - Enforce uniqueness across lists with priority:
    - favorites first
    - least-often-in-top second
    - discovery last

Note: discovery random tie handling from the original draft plan was superseded by the updated rule: discovery is now strictly unchosen dishes.

### 5) Controller endpoint
- Updated `src/main/java/com/camlong/homnayangi/controller/DishRotationController.java`
- Added:

```java
@GetMapping("/recommendations")
public ResponseEntity<DishChoiceRecommendation> getRecommendations(Authentication authentication) {
    return ResponseEntity.ok(dishRotationService.getRecommendations(authentication.getName()));
}
```

### 6) Unit tests
- Added `src/test/java/unit/com/camlong/homnayangi/service/impl/DishRotationServiceImplTest.java`
- Tests cover:
  - top favorites ranking
  - discovery includes unchosen dishes only
  - last 5 from top 15 behavior
  - small/empty-history behavior
  - metadata fields (`timesChosen`, `lastChosenTime`)
  - uniqueness across lists

## Edge Cases (Current Behavior)
- User has no choices: `userFavorites` and `userLeastOftenInTop` are empty, `userDiscovery` contains all dishes.
- Total chosen dishes < 10: favorites returns available chosen dishes.
- Total chosen dishes < 15: least-often-in-top returns fewer or empty after dedupe.
- Ties in chosen ranking are deterministic via `dish.id` ascending.

## Validation
- Verified locally with:
  - `./gradlew test --no-daemon`
- Result: `BUILD SUCCESSFUL`.

## Optional Follow-up
- OpenAPI spec update was not applied in this implementation pass.
