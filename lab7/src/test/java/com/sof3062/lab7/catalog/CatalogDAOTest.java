package com.sof3062.lab7.catalog;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogDAOTest {

  private CategoryDAO categoryDAO;
  private ProductDAO productDAO;

  @BeforeEach
  void setUp() {
    categoryDAO = new CategoryDAO();
    productDAO = new ProductDAO();
    CategoryDAO.DB.clear();
    ProductDAO.DB.clear();
  }

  @Test
  void categoryCRUD_shouldWork() {
    Category category = Category.builder().name("Electronics").build();
    Category saved = categoryDAO.save(category);

    assertNotNull(saved.getId());
    assertEquals("Electronics", saved.getName());

    Optional<Category> found = categoryDAO.findById(saved.getId());
    assertTrue(found.isPresent());

    categoryDAO.delete(saved.getId());
    assertTrue(categoryDAO.findById(saved.getId()).isEmpty());
  }

  @Test
  void productCRUD_shouldWork() {
    Product product = Product.builder()
      .name("Laptop")
      .price(1000.0)
      .categoryId(1L)
      .build();
    Product saved = productDAO.save(product);

    assertNotNull(saved.getId());
    assertEquals("Laptop", saved.getName());

    Optional<Product> found = productDAO.findById(saved.getId());
    assertTrue(found.isPresent());

    List<Product> byCategory = productDAO.findByCategoryId(1L);
    assertEquals(1, byCategory.size());

    productDAO.delete(saved.getId());
    assertTrue(productDAO.findById(saved.getId()).isEmpty());
  }
}
