package com.camlong.homnayangi.controller;

import com.camlong.homnayangi.service.DishRotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cuisine/rotation")
@PreAuthorize("hasAnyRole('Admin', 'User')")
public class DishRotationController {

    private final DishRotationService dishRotationService;
}
