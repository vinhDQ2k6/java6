package com.sof3062.lab7.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  private Long id;
  private String name;
  private Double price;
  private Long categoryId;
}
