package com.sof3062.lab7.identity;

import com.sof3062.lab7.infrastructure.security.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AccountDAO accountDAO;
  private final JwtUtil jwtUtil;

  public String login(String username, String password) {
    Optional<Account> account = accountDAO.findByUsername(username);
    if (account.isPresent() && account.get().getPassword().equals(password)) {
      return jwtUtil.generateToken(username);
    }
    throw new RuntimeException("Invalid credentials");
  }

  public Account register(Account account) {
    if (accountDAO.findByUsername(account.getUsername()).isPresent()) {
      throw new RuntimeException("Username already exists");
    }
    return accountDAO.save(account);
  }
}
