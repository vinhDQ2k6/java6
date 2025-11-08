package com.sof3062.security;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sof3062.dao.UserDAO;
import com.sof3062.web.model.User;

public class DaoUserDetailsManager implements UserDetailsService {
    UserDAO dao;

    public DaoUserDetailsManager(UserDAO userDAO) {
        this.dao = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username, "username must not be null");
        Optional<User> opt = dao.findById(username);
        User user = opt.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        String password = user.getPassword();
        String[] roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getId().substring(5))
                .toList().toArray(new String[0]);
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(password)
                .roles(roles)
                .build();
    }
}