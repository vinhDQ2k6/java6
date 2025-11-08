package com.sof3062.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.sof3062.dao.UserDAO;
import com.sof3062.security.DaoUserDetailsManager;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
public class SecurityConfig {
        UserDAO dao;

        public SecurityConfig(UserDAO userDAO) {
                this.dao = userDAO;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                return new DaoUserDetailsManager(dao);
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