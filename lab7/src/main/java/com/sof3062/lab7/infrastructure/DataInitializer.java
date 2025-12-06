package com.sof3062.lab7.infrastructure;

import com.sof3062.lab7.catalog.Category;
import com.sof3062.lab7.catalog.CategoryDAO;
import com.sof3062.lab7.catalog.Product;
import com.sof3062.lab7.catalog.ProductDAO;
import com.sof3062.lab7.identity.Account;
import com.sof3062.lab7.identity.AccountDAO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final AccountDAO accountDAO;
  private final CategoryDAO categoryDAO;
  private final ProductDAO productDAO;

  @Override
  public void run(String... args) throws Exception {
    if (!accountDAO.findAll().isEmpty()) {
      return;
    }

    // Accounts
    Account admin = Account.builder()
      .username("admin")
      .password("admin") // In real app, encrypt this
      .email("admin@example.com")
      .roles(List.of("ADMIN", "USER"))
      .build();
    accountDAO.save(admin);

    Account user = Account.builder()
      .username("user")
      .password("user")
      .email("user@example.com")
      .roles(List.of("USER"))
      .build();
    accountDAO.save(user);

    // Categories
    Category electronics = Category.builder().name("Electronics").build();
    categoryDAO.save(electronics); // ID 1

    Category books = Category.builder().name("Books").build();
    categoryDAO.save(books); // ID 2

    // Products
    Product laptop = Product.builder()
      .name("Laptop")
      .price(1200.0)
      .categoryId(electronics.getId())
      .build();
    productDAO.save(laptop);

    Product phone = Product.builder()
      .name("Smartphone")
      .price(800.0)
      .categoryId(electronics.getId())
      .build();
    productDAO.save(phone);

    Product novel = Product.builder()
      .name("The Great Gatsby")
      .price(15.0)
      .categoryId(books.getId())
      .build();
    productDAO.save(novel);

    System.out.println(
      "Sample data initialized: 2 Accounts, 2 Categories, 3 Products"
    );
  }
}
