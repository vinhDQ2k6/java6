package com.sof3062.lab7.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDAO {

  public static final List<Product> DB = new ArrayList<>();
  private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

  public Product save(Product product) {
    if (product.getId() == null) {
      product.setId(ID_GENERATOR.getAndIncrement());
    } else {
      delete(product.getId());
    }
    DB.add(product);
    return product;
  }

  public Optional<Product> findById(Long id) {
    return DB.stream().filter(p -> p.getId().equals(id)).findFirst();
  }

  public List<Product> findAll() {
    return new ArrayList<>(DB);
  }

  public List<Product> findByCategoryId(Long categoryId) {
    return DB.stream()
      .filter(p -> p.getCategoryId().equals(categoryId))
      .collect(Collectors.toList());
  }

  public void delete(Long id) {
    DB.removeIf(p -> p.getId().equals(id));
  }
}
