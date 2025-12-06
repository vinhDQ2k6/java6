package com.sof3062.lab7.catalog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sof3062.lab7.infrastructure.security.JwtUtil;
import com.sof3062.lab7.infrastructure.security.SecurityConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CatalogController.class)
@Import({ SecurityConfig.class, JwtUtil.class })
class CatalogControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CatalogService catalogService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void getAllCategories_shouldReturnList() throws Exception {
    Category category = Category.builder().id(1L).name("Electronics").build();
    when(catalogService.getAllCategories()).thenReturn(List.of(category));

    mockMvc
      .perform(get("/api/catalog/categories"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].name").value("Electronics"));
  }

  @Test
  @WithMockUser
  void createProduct_shouldReturnProduct_whenSuccess() throws Exception {
    Product product = Product.builder()
      .name("Laptop")
      .price(1000.0)
      .categoryId(1L)
      .build();
    Product savedProduct = Product.builder()
      .id(1L)
      .name("Laptop")
      .price(1000.0)
      .categoryId(1L)
      .build();

    when(catalogService.createProduct(any(Product.class))).thenReturn(
      savedProduct
    );

    mockMvc
      .perform(
        post("/api/catalog/products")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(product))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(1));
  }
}
