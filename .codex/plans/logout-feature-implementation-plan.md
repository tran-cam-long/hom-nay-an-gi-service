# Logout Feature Implementation Plan

## Goal
Implement a logout flow that invalidates refresh tokens in persistence so clients cannot obtain new access tokens after logout.

## Current State (based on codebase)
- `POST /api/auth/login` returns `AuthResponse` and persists refresh token in `refresh_tokens` table.
- `POST /api/auth/refresh` validates refresh token from DB, checks expiry, and issues a new access token.
- No logout endpoint exists in `AuthController`.
- `RefreshTokenRepository` supports lookup by token and delete by username only.
- Security permits unauthenticated access only to `/api/auth/login`, `/api/auth/register`, `/api/auth/refresh`.

## Proposed Logout Behavior
- Add `POST /api/auth/logout`.
- Request will contain `refreshToken` (query param for consistency with existing `/refresh`, or request body if you want a cleaner API contract).
- Server deletes the corresponding refresh token row.
- Endpoint returns `204 No Content`.
- Refresh token not found should still return success (`204`) to keep logout idempotent and avoid token probing.

## Step-by-Step Implementation

### 1. Define API contract for logout
- Decide input shape:
  - Option A (minimal change): `POST /api/auth/logout?refreshToken=...`
  - Option B (recommended long-term): JSON body DTO, e.g. `{ "refreshToken": "..." }`
- Decide response code:
  - Use `204 No Content` for successful and idempotent logout.
- Document contract in `README.md` or API docs.

### 2. Extend service interface
- File: `src/main/java/com/camlong/homnayangi/service/AuthService.java`
- Add method signature:
  - `void logout(String refreshToken);`

### 3. Implement logout in service layer
- File: `src/main/java/com/camlong/homnayangi/service/impl/AuthServiceImpl.java`
- Add `logout(String refreshToken)` implementation.
- Logic:
  - Validate `refreshToken` input is not blank.
  - Delete token by exact token value.
  - Do not throw if token does not exist (idempotency).
- Logging:
  - Add info/debug logs for traceability without leaking full token value.

### 4. Add repository support for token deletion
- File: `src/main/java/com/camlong/homnayangi/repository/RefreshTokenRepository.java`
- Add method:
  - `void deleteByToken(String token);`
- Optional improvement:
  - add `boolean existsByToken(String token);` if you want metrics/logging on whether a token existed before delete.

### 5. Add controller endpoint
- File: `src/main/java/com/camlong/homnayangi/controller/AuthController.java`
- Add endpoint:
  - `@PostMapping("/logout")`
- Delegate to `authService.logout(...)`.
- Return `ResponseEntity.noContent().build()`.
- Keep parameter validation consistent with existing endpoints.

### 6. Security configuration decision
- File: `src/main/java/com/camlong/homnayangi/config/SecurityConfig.java`
- Choose one policy:
  - Policy A (simple): allow `/api/auth/logout` as `permitAll()` because logout is driven by refresh token invalidation.
  - Policy B (stricter): require access token authentication for logout and validate the token belongs to the authenticated principal.
- For current architecture and minimum disruption, Policy A is easier and consistent with refresh flow.

### 7. Improve token lifecycle (recommended while implementing logout)
- File: `src/main/java/com/camlong/homnayangi/service/impl/AuthServiceImpl.java`
- Before storing a new refresh token on login, consider deleting previous token(s) for same username:
  - call `refreshTokenRepository.deleteByUsername(username);` before `save(...)`.
- This prevents multiple valid refresh tokens per user if that is desired.

### 8. Add unit tests for service logic
- File: `src/test/java/unit/com/camlong/homnayangi/service/impl/AuthServiceImplTest.java`
- Add tests:
  - `givenExistingRefreshToken_whenLogout_thenDeleteByTokenCalled`
  - `givenNonExistingRefreshToken_whenLogout_thenStillSucceed`
  - `givenBlankRefreshToken_whenLogout_thenThrowRuntimeException` (if you enforce input validation)
- Verify repository interaction with Mockito.

### 9. Add unit tests for controller
- File: `src/test/java/unit/com/camlong/homnayangi/controller/AuthControllerTest.java`
- Add tests:
  - valid logout request returns `204`
  - controller delegates to `authService.logout(...)`
  - invalid request shape returns `400` (if validation added)

### 10. Add/adjust component test coverage
- File: `src/test/java/component/com/camlong/homnayangi/ComponentTest.java`
- Add integration scenario:
  - login -> obtain refresh token
  - logout with that refresh token
  - refresh with same token should fail (`RuntimeException` mapped error)
- This verifies end-to-end token invalidation behavior.

### 11. Error handling and exception consistency
- Files:
  - `src/main/java/com/camlong/homnayangi/config/exception/GlobalExceptionHandler.java`
  - `src/main/java/com/camlong/homnayangi/config/exception/BusinessException.java`
- Replace generic `RuntimeException` in auth flow with domain-specific exceptions (optional but recommended).
- Ensure logout uses predictable response semantics and does not leak sensitive token details.

### 12. Manual verification checklist
- `POST /api/auth/login` still works.
- `POST /api/auth/refresh` works before logout.
- `POST /api/auth/logout` returns `204`.
- `POST /api/auth/refresh` with logged-out token fails.
- Re-login provides a new refresh token and refresh works again.

### 13. Non-functional considerations
- Add audit logging for logout attempts.
- Rate limit `/api/auth/logout` similarly to other auth endpoints.
- Consider future support for:
  - logout-all-devices (`deleteByUsername`)
  - JWT blacklist for immediate access-token revocation (if required by security policy)

## Suggested Implementation Order
1. Repository method `deleteByToken`.
2. Service interface + implementation `logout`.
3. Controller endpoint `/logout`.
4. Security config update.
5. Unit tests (service, controller).
6. Component test.
7. API documentation update.

## Acceptance Criteria
- New endpoint `POST /api/auth/logout` exists and returns `204`.
- Given a refresh token used for logout, subsequent `/api/auth/refresh` with that token fails.
- Existing login/register/refresh behavior remains functional.
- New and updated tests pass.
