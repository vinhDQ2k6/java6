package com.sof3062.lab7.identity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
      String token = authService.login(
        request.getUsername(),
        request.getPassword()
      );
      return ResponseEntity.ok(new AuthResponse(token));
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Account account) {
    try {
      return ResponseEntity.ok(authService.register(account));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @Data
  public static class LoginRequest {

    private String username;
    private String password;
  }

  @Data
  @AllArgsConstructor
  public static class AuthResponse {

    private String token;
  }
}
