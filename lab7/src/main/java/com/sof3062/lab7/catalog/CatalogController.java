package com.sof3062.lab7.catalog;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

  private final CatalogService catalogService;

  @GetMapping("/categories")
  public ResponseEntity<List<Category>> getAllCategories() {
    return ResponseEntity.ok(catalogService.getAllCategories());
  }

  @PostMapping("/categories")
  public ResponseEntity<Category> createCategory(
    @RequestBody Category category
  ) {
    return ResponseEntity.ok(catalogService.createCategory(category));
  }

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAllProducts() {
    return ResponseEntity.ok(catalogService.getAllProducts());
  }

  @PostMapping("/products")
  public ResponseEntity<?> createProduct(@RequestBody Product product) {
    try {
      return ResponseEntity.ok(catalogService.createProduct(product));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/categories/{id}")
  public ResponseEntity<?> updateCategory(
    @PathVariable Long id,
    @RequestBody Category category
  ) {
    try {
      return ResponseEntity.ok(catalogService.updateCategory(id, category));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/categories/{id}")
  public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
    catalogService.deleteCategory(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<?> updateProduct(
    @PathVariable Long id,
    @RequestBody Product product
  ) {
    try {
      return ResponseEntity.ok(catalogService.updateProduct(id, product));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/products/{id}")
  public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    catalogService.deleteProduct(id);
    return ResponseEntity.ok().build();
  }
}
