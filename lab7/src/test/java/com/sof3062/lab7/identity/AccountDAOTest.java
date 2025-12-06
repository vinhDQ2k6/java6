package com.sof3062.lab7.identity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountDAOTest {

  private AccountDAO accountDAO;

  @BeforeEach
  void setUp() {
    accountDAO = new AccountDAO();
    AccountDAO.DB.clear();
  }

  @Test
  void save_shouldAddAccountToDB() {
    Account account = Account.builder()
      .username("user1")
      .password("pass1")
      .email("user1@example.com")
      .roles(List.of("USER"))
      .build();

    Account saved = accountDAO.save(account);

    assertNotNull(saved);
    assertEquals("user1", saved.getUsername());
    assertEquals(1, AccountDAO.DB.size());
  }

  @Test
  void findByUsername_shouldReturnAccount_whenExists() {
    Account account = Account.builder().username("user1").build();
    AccountDAO.DB.add(account);

    Optional<Account> found = accountDAO.findByUsername("user1");

    assertTrue(found.isPresent());
    assertEquals("user1", found.get().getUsername());
  }

  @Test
  void findByUsername_shouldReturnEmpty_whenNotExists() {
    Optional<Account> found = accountDAO.findByUsername("user1");
    assertTrue(found.isEmpty());
  }
}
