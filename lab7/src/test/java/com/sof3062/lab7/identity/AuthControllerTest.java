package com.sof3062.lab7.identity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sof3062.lab7.infrastructure.security.JwtUtil;
import com.sof3062.lab7.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import({ SecurityConfig.class, JwtUtil.class })
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
    AuthController.LoginRequest loginRequest =
      new AuthController.LoginRequest();
    loginRequest.setUsername("user1");
    loginRequest.setPassword("pass1");

    when(authService.login("user1", "pass1")).thenReturn("fake-jwt-token");

    mockMvc
      .perform(
        post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(loginRequest))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").value("fake-jwt-token"));
  }

  @Test
  void register_shouldReturnAccount_whenSuccess() throws Exception {
    Account account = Account.builder()
      .username("user1")
      .password("pass1")
      .email("user1@example.com")
      .build();

    when(authService.register(any(Account.class))).thenReturn(account);

    mockMvc
      .perform(
        post("/api/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(account))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.username").value("user1"));
  }
}
