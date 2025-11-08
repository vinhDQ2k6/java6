package com.sof3062.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder pe) {
                String encoded = pe.encode("123");

                return new InMemoryUserDetailsManager(
                                createUser("admin@gmail.com", encoded, "ADMIN"),
                                createUser("user@gmail.com", encoded, "USER"),
                                createUser("both@gmail.com", encoded, "ADMIN", "USER"));
        }

        private UserDetails createUser(String username, String password, String... roles) {
                return User
                                .withUsername(username.toLowerCase())
                                .password(password)
                                .roles(roles)
                                .build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http.csrf(CsrfConfigurer::disable).cors(CorsConfigurer::disable)
                                .authorizeHttpRequests(config -> config
                                                .requestMatchers("/poly/url1").authenticated()
                                                .requestMatchers("/poly/url2").hasRole("USER")
                                                .requestMatchers("/poly/url3").hasRole("ADMIN")
                                                .requestMatchers("/poly/url4").hasAnyRole("USER", "ADMIN")
                                                .anyRequest().permitAll())
                                .exceptionHandling(ad -> ad.accessDeniedPage("/403"))
                                .formLogin(config -> config
                                                .loginPage("/login/form")
                                                .loginProcessingUrl("/login/check")
                                                .defaultSuccessUrl("/login/success", true)
                                                .failureUrl("/login/failure")
                                                .permitAll()
                                                .usernameParameter("username")
                                                .passwordParameter("password"))
                                .rememberMe(rm -> rm
                                                .tokenValiditySeconds(3 * 24 * 60 * 60)
                                                .rememberMeCookieName("remember-me")
                                                .rememberMeParameter("remember-me"))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .deleteCookies("JSESSIONID", "remember-me")
                                                .clearAuthentication(true)
                                                .invalidateHttpSession(true)
                                                .deleteCookies("remember-me"))
                                .build();
        }
}