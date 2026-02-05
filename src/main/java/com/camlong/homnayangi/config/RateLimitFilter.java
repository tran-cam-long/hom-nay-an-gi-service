package com.camlong.homnayangi.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

  private final Bucket bucket;

  public RateLimitFilter() {
    bucket = Bucket.builder()
        .addLimit(Bandwidth.simple(10, Duration.ofMinutes(1)))
        .build();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (request.getRequestURI().startsWith("/api/auth")) {
      if (!bucket.tryConsume(1)) {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
