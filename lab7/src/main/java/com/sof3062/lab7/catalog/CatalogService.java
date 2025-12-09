package com.sof3062.lab7.catalog;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogService {

  private final CategoryDAO categoryDAO;
  private final ProductDAO productDAO;

  public List<Category> getAllCategories() {
    return categoryDAO.findAll();
  }

  public Category createCategory(Category category) {
    return categoryDAO.save(category);
  }

  public List<Product> getAllProducts() {
    return productDAO.findAll();
  }

  public Product createProduct(Product product) {
    if (categoryDAO.findById(product.getCategoryId()).isEmpty()) {
      throw new RuntimeException("Category not found");
    }
    return productDAO.save(product);
  }

  public List<Product> getProductsByCategory(Long categoryId) {
    return productDAO.findByCategoryId(categoryId);
  }

  public Category updateCategory(Long id, Category category) {
    if (categoryDAO.findById(id).isEmpty()) {
      throw new RuntimeException("Category not found");
    }
    category.setId(id);
    return categoryDAO.save(category);
  }

  public void deleteCategory(Long id) {
    categoryDAO.delete(id);
  }

  public Product updateProduct(Long id, Product product) {
    if (productDAO.findById(id).isEmpty()) {
      throw new RuntimeException("Product not found");
    }
    if (categoryDAO.findById(product.getCategoryId()).isEmpty()) {
      throw new RuntimeException("Category not found");
    }
    product.setId(id);
    return productDAO.save(product);
  }

  public void deleteProduct(Long id) {
    productDAO.delete(id);
  }
}
