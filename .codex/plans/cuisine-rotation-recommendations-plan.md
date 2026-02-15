# Plan: `GET /api/cuisine/rotation/recommendations`

## Goal
Add a new endpoint that returns personalized dish recommendation buckets from `DishChoice` history:
- `userFavorites`: top 10 most chosen dishes.
- `userDiscovery`: top 10 least chosen dishes. If many dishes are tied at the least-chosen boundary, pick randomly among tied candidates.
- `userLeastOftenInTop`: take top 15 most chosen, then return the last 5 of that list (ranks 11-15).

`DishChoice` is the source of user choice history (`src/main/java/com/camlong/homnayangi/entity/DishChoice.java`).
Use `src/main/java/com/camlong/homnayangi/dto/DishChoiceRecommendation.java` as the response DTO.

## API Contract
- Method: `GET`
- Path: `/api/cuisine/rotation/recommendations`
- Auth: same as existing `DishRotationController` (`@PreAuthorize("hasAnyRole('Admin', 'User')")`)
- Response `200`:

```json
{
  "userFavorites": [{ "id": 1, "name": "...", "type": "...", "culture": "...", "imageUrl": "..." }],
  "userDiscovery": [{ "id": 2, "name": "...", "type": "...", "culture": "...", "imageUrl": "..." }],
  "userLeastOftenInTop": [{ "id": 3, "name": "...", "type": "...", "culture": "...", "imageUrl": "..." }]
}
```

## Implementation Steps

### 1) Define DTOs 
1. Update `src/main/java/com/camlong/homnayangi/dto/DishChoiceRecommendation.java` to a record:
```java
public record DishChoiceRecommendation(
    List<CuisineDish> userFavorites,
    List<CuisineDish> userDiscovery,
    List<CuisineDish> userLeastOftenInTop
) {}
```
2. Add imports for `CuisineDish` and `List`.
3. Keep it simple: return full `CuisineDish` objects (current codebase already returns entities in responses).

### 2) Add aggregation query support 
1. Add a small projection record in `src/main/java/com/camlong/homnayangi/dto/`:
```java
public record DishChoiceCount(Long dishId, long choiceCount) {}
```
2. Update `src/main/java/com/camlong/homnayangi/repository/DishChoiceRepository.java`:
- Add query:
```java
@Query("""
    select new com.camlong.homnayangi.dto.DishChoiceCount(dc.dish.id, count(dc.id))
    from DishChoice dc
    where dc.user.id = :userId
    group by dc.dish.id
""")
List<DishChoiceCount> findChoiceCountByUserId(@Param("userId") Long userId);
```
3. Add imports for `@Query`, `@Param`, and the new DTO.

### 3) Extend service interface 
1. Update `src/main/java/com/camlong/homnayangi/service/DishRotationService.java`:
```java
DishChoiceRecommendation getRecommendations(String username);
```
2. Add DTO import.

### 4) Implement recommendation algorithm 
1. Update `src/main/java/com/camlong/homnayangi/service/impl/DishRotationServiceImpl.java`:
- Inject no new beans; reuse:
  - `ApplicationUserRepository`
  - `CuisineDishRepository`
  - `DishChoiceRepository`
2. Implement:
```java
@Override
public DishChoiceRecommendation getRecommendations(String username) { ... }
```
3. Algorithm details:
- Load user by username (same error handling pattern as `recordChoice`).
- Load all dishes once: `cuisineDishRepository.findAll()`.
- Load counts map from repository query.
- Build `DishWithCount` in memory for every dish with default count `0` if absent.
- Sorting rules:
  - `mostChosen`: sort by `count DESC`, tie-break by `dish.id ASC`.
  - `leastChosen`: sort by `count ASC`, tie-break by `dish.id ASC`.
- `userFavorites`:
  - first 10 from `mostChosen`.
- `userLeastOftenInTop`:
  - `top15 = first 15 from mostChosen`.
  - return last 5 from `top15` (or fewer if <5 total).
- `userDiscovery` (random tie handling):
  - Start from `leastChosen`.
  - If more than 10 items and boundary count is tied:
    - let `cutoffCount = count at index 9`.
    - take all items with `count < cutoffCount` directly.
    - collect all items with `count == cutoffCount`, `Collections.shuffle(...)`, then take only needed items to reach 10.
  - If <=10 items, return all.
4. Return `new DishChoiceRecommendation(...)` with lists of `CuisineDish` only.

### 5) Add controller endpoint 
1. Update `src/main/java/com/camlong/homnayangi/controller/DishRotationController.java`:
```java
@GetMapping("/recommendations")
public ResponseEntity<DishChoiceRecommendation> getRecommendations(Authentication authentication) {
    return ResponseEntity.ok(dishRotationService.getRecommendations(authentication.getName()));
}
```
2. Add imports for `GetMapping` and `DishChoiceRecommendation`.

### 6) Unit tests for service logic 
Create `src/test/java/unit/com/camlong/homnayangi/service/impl/DishRotationServiceImplTest.java` (or extend if exists) with Mockito:
1. `getRecommendations_shouldReturnTop10Favorites`
2. `getRecommendations_shouldReturnDiscoveryWithRandomSelectionOnBoundaryTie`
3. `getRecommendations_shouldReturnLast5FromTop15`
4. `getRecommendations_shouldHandleFewerThanThresholds`

Test setup guidance:
- Mock user lookup.
- Mock `cuisineDishRepository.findAll()` with 20+ dishes.
- Mock `dishChoiceRepository.findChoiceCountByUserId(...)` with selected counts.
- For random boundary test, assert size and count constraints (avoid asserting exact order of shuffled subset).

### 7) Optional API doc update 
If your team keeps OpenAPI updated manually, add this endpoint in the API spec location used by your project (if any).

## Edge Cases to Lock In
- User has no choices yet: all dishes have count `0`.
- Total dishes < 10 or < 15: return available size only.
- Ties for favorites/least-often-in-top: deterministic via `dish.id` tie-break.
- Ties at discovery cutoff: randomized selection only for the tied boundary group.

## Definition of Done
1. `GET /api/cuisine/rotation/recommendations` returns the 3 required lists.
2. `DishChoiceRecommendation` contains exactly `userFavorites`, `userDiscovery`, `userLeastOftenInTop`.
3. Random tie behavior for `userDiscovery` is implemented at cutoff.
4. Unit tests cover thresholds, tie handling, and empty-history behavior.
5. Build/tests pass locally.
