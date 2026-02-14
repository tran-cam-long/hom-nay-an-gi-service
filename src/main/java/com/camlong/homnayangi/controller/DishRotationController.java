package com.camlong.homnayangi.controller;

import com.camlong.homnayangi.dto.DishChoiceSubmitRequest;
import com.camlong.homnayangi.service.DishRotationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cuisine/rotation")
@PreAuthorize("hasAnyRole('Admin', 'User')")
public class DishRotationController {

    private final DishRotationService dishRotationService;

    @PostMapping("/choice")
    public ResponseEntity<Void> submitChoice(@Valid @RequestBody DishChoiceSubmitRequest request, Authentication authentication) {
        dishRotationService.recordChoice(authentication.getName(), request.dishId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
