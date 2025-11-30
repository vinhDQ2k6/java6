package com.sof3062.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sof3062.dto.LoginRequest;
import com.sof3062.service.JwtService;

/**
 * Controller responsible for handling user authentication.
 */
@RestController
public class LoginController {

    @Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

    /**
     * Authenticates a user and returns a JWT token.
     * 
     * @param loginRequest The login request containing username and password.
     * @return A ResponseEntity containing the JWT token if successful.
     * @throws UsernameNotFoundException if authentication fails.
     */
	@PostMapping("/poly/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
		Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

		if (auth.isAuthenticated()) {
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String token = jwtService.createToken(userDetails, 20 * 60); // 20 minutes validity
			return ResponseEntity.ok(Map.of("token", token));
		} 
		throw new UsernameNotFoundException("Invalid username or password!");
	}
}
