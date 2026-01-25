package com.camlong.homnayangi.inbound.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/cuisine/dishes")
public class CuisineDishController {

  @GetMapping
  public ResponseEntity<Void> findById(@PathVariable String id) {
    return ResponseEntity.ok(null);
  }
}
