package com.sof3062.lab7.identity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  private String username;
  private String password;
  private String email;
  private List<String> roles;
}
