package com.sof3062.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import com.sof3062.dao.UserDAO;
import com.sof3062.security.DaoUserDetailsManager;
import com.sof3062.service.AuthService;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        private final AuthService auth;
        private final UserDAO dao;

        public SecurityConfig(UserDAO userDAO, AuthService auth) {
                this.dao = userDAO;
                this.auth = auth;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        // @Bean
        // public UserDetailsService userDetailsService(DataSource dataSource) {
        // String userSQL = "SELECT username, password, enabled FROM users WHERE
        // username = ?";
        // String roleSQL = "SELECT u.username, ur.roles FROM users u INNER JOIN
        // user_roles ur ON u.username = ur.username WHERE u.username = ?";
        // JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        // manager.setUsersByUsernameQuery(userSQL);
        // manager.setAuthoritiesByUsernameQuery(roleSQL);

        // return manager;
        // }

        @Bean
        public UserDetailsService userDetailsService() {
                return new DaoUserDetailsManager(dao);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .csrf(CsrfConfigurer::disable)
                        .cors(CorsConfigurer::disable)
                        .authorizeHttpRequests(req -> req.anyRequest().permitAll())
                        .exceptionHandling(ad -> ad.accessDeniedPage("/403"))
                        .formLogin(config -> config
                                        .loginPage("/login/form")
                                        .loginProcessingUrl("/login/check")
                                        .defaultSuccessUrl("/login/success", true)
                                        .failureUrl("/login/failure")
                                        .usernameParameter("username")
                                        .passwordParameter("password")
                        )
                        .oauth2Login(config -> config
                                        .permitAll()
                                        .successHandler((request, response, authentication) -> {                                                
                                                // Lấy thông tin người dùng từ authentication
                                                DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
                                                var username = user.getEmail().substring(0, user.getEmail().indexOf("@"));
                                                var role = "USER";

                                                // Kiểm tra nếu người dùng chưa tồn tại trong hệ thống, thì tạo mới
                                                UserDetails newUser = User
                                                        .withUsername(username)
                                                        .password("{noop}")
                                                        .roles(role) // Defined above as "USER"
                                                        .build();
                                                UsernamePasswordAuthenticationToken newAuth = new 
                                                        UsernamePasswordAuthenticationToken(
                                                                newUser,
                                                                null,
                                                                newUser.getAuthorities()
                                                        );

                                                // Cập nhật authentication trong SecurityContext
                                                SecurityContextHolder.getContext().setAuthentication(newAuth);

                                                // Xử lý sau khi đăng nhập thành công
                                                var session = request.getSession();
                                                var req = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                                                response.sendRedirect(
                                                        req == null ? "/" : req.getRedirectUrl()
                                                );
                                        })
                        )
                        .rememberMe(rm -> rm
                                        .tokenValiditySeconds(3 * 24 * 60 * 60)
                                        .rememberMeCookieName("remember-me")
                                        .rememberMeParameter("remember-me")
                        )
                        .logout(logout -> logout
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/")
                                        .deleteCookies("JSESSIONID", "remember-me")
                                        .clearAuthentication(true)
                                        .invalidateHttpSession(true)
                                        .deleteCookies("remember-me")
                        )
                        .build();
        }
}