package com.camlong.homnayangi.inbound.apis;

import com.camlong.homnayangi.application.cuisine.CuisineDishService;
import com.camlong.homnayangi.domain.models.cusine.CuisineDish;
import com.camlong.homnayangi.inbound.models.requests.CreateCuisineDishRequest;
import com.camlong.homnayangi.inbound.models.responses.CreateCuisineDishResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.camlong.homnayangi.application.constants.ApplicationConstants.ROLE_ADMIN;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/cuisine/dishes")
@PreAuthorize(ROLE_ADMIN)
public class CuisineDishController {

  private final CuisineDishService cuisineDishService;

  @GetMapping("/{id}")
  public ResponseEntity<@NonNull CuisineDish> findById(@PathVariable("id") Long id) {
    final CuisineDish cuisineDish = cuisineDishService.findById(id);
    return ResponseEntity.ok(cuisineDish);
  }

  @PostMapping
  public ResponseEntity<@NonNull CreateCuisineDishResponse> save(@RequestBody CreateCuisineDishRequest request) {
    final CuisineDish toCreate = CuisineDish.builder()
        .name(request.name())
        .type(request.type())
        .culture(request.culture())
        .imageUrl(request.imageUrl())
        .searchKeyword(request.searchKeyword())
        .build();

    final CuisineDish saved = cuisineDishService.createNewDish(toCreate);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CreateCuisineDishResponse.builder().id(saved.getId()).build());
  }

  @PatchMapping
  public ResponseEntity<@NonNull Void> patchDish(
      @PathVariable Long id,
      @RequestBody JsonNode request) {

    cuisineDishService.patchDish(id, request);

    return ResponseEntity.ok(null);
  }
}
