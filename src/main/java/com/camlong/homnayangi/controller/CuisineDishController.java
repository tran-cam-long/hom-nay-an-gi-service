package com.camlong.homnayangi.controller;

import com.camlong.homnayangi.dto.CreateCuisineDishRequest;
import com.camlong.homnayangi.entity.CuisineDish;
import com.camlong.homnayangi.service.CuisineDishService;
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

import static com.camlong.homnayangi.constant.ApplicationConstants.ROLE_ADMIN;

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
  public ResponseEntity<@NonNull CuisineDish> save(@RequestBody CreateCuisineDishRequest request) {
    final CuisineDish toCreate = CuisineDish.builder()
        .name(request.name())
        .type(request.type())
        .culture(request.culture())
        .imageUrl(request.imageUrl())
        .searchKeyword(request.searchKeyword())
        .build();

    final CuisineDish saved = cuisineDishService.createNewDish(toCreate);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PatchMapping
  public ResponseEntity<@NonNull Void> patchDish(
      @PathVariable Long id,
      @RequestBody JsonNode request) {

    cuisineDishService.patchDish(id, request);

    return ResponseEntity.ok(null);
  }
}
