package com.sof3062.lab7.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAO {

  public static final List<Category> DB = new ArrayList<>();
  private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

  public Category save(Category category) {
    if (category.getId() == null) {
      category.setId(ID_GENERATOR.getAndIncrement());
    } else {
      delete(category.getId());
    }
    DB.add(category);
    return category;
  }

  public Optional<Category> findById(Long id) {
    return DB.stream().filter(c -> c.getId().equals(id)).findFirst();
  }

  public List<Category> findAll() {
    return new ArrayList<>(DB);
  }

  public void delete(Long id) {
    DB.removeIf(c -> c.getId().equals(id));
  }
}
