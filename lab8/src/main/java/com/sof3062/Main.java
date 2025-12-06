package com.sof3062;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 * <p>
 * This class bootstraps the application, starting the embedded Tomcat server
 * and initializing the Spring context.
 * </p>
 */
@SpringBootApplication
public class Main {

    /**
     * The main method that starts the application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
