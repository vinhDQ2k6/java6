package com.sof3062.service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Service for handling JSON Web Token (JWT) operations such as creation, validation, and parsing.
 */
@Service
public class JwtService {

    // Ideally, this should be in application.properties
    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    /**
     * Creates a JWT for the given user.
     *
     * @param userDetails   The user details to include in the token.
     * @param expirySeconds The validity period of the token in seconds.
     * @return The generated JWT string.
     */
	public String createToken(UserDetails userDetails, int expirySeconds) {
		long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(Map.of("publisher", "Poly"))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000L * expirySeconds)) 
                .signWith(this.getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the JWT against the user details.
     *
     * @param token       The JWT string.
     * @param userDetails The user details to check against.
     * @return true if the token is valid and belongs to the user, false otherwise.
     */
	public boolean validateToken(String token, UserDetails userDetails) {
    	Claims claims = this.getClaims(token);
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
    }

    /**
     * Extracts the username from the JWT.
     *
     * @param token The JWT string.
     * @return The username (subject) contained in the token.
     */
	public String getUsername(String token) {
    	Claims claims = this.getClaims(token);
    	return claims.getSubject();
    }

    /**
     * Generates the signing key from the secret.
     *
     * @return The Key object used for signing.
     */
    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET); // 32 bytes
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Parses the JWT to extract claims.
     *
     * @param token The JWT string.
     * @return The Claims object containing the token payload.
     */
	private Claims getClaims(String token) {
    	return Jwts.parserBuilder()
                .setSigningKey(this.getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
