## Setup

### JWT - Signed Claims JWSs are not supported.
- From extractClaims(), look for parseClaimsJwt, change to parseClaimsJws

### Mapstruct - generated mapping logic does not include mapping from parent class.
- Check if parent class annotated with SuperBuilder or not