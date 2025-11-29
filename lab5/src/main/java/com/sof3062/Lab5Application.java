package com.sof3062;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Lab5Application {

  public static void main(String[] args) {
    SpringApplication.run(Lab5Application.class, args);
  }
}
