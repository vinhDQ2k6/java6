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
}
