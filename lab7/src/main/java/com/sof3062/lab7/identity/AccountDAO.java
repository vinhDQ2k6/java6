package com.sof3062.lab7.identity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO {

  public static final List<Account> DB = new ArrayList<>();

  public Account save(Account account) {
    DB.add(account);
    return account;
  }

  public Optional<Account> findByUsername(String username) {
    return DB.stream()
      .filter(a -> a.getUsername().equals(username))
      .findFirst();
  }

  public List<Account> findAll() {
    return new ArrayList<>(DB);
  }

  public void delete(String username) {
    DB.removeIf(a -> a.getUsername().equals(username));
  }
}
