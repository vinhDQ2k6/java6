package com.sof3062.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller handling various endpoints for testing security roles and access.
 */
@RestController
public class PolyController {

    /**
     * Public endpoint accessible to everyone.
     * @return A map containing the URL and method name.
     */
    @GetMapping({"/poly/url0", "/"})
	public ResponseEntity<Map<String, String>> method0() {
		return ResponseEntity.ok(Map.of("url", "/poly/url0", "method", "method0()"));
	}
	
    /**
     * Endpoint requiring authentication.
     * @return A map containing the URL and method name.
     */
	@GetMapping("/poly/url1")
	public ResponseEntity<Map<String, String>> method1() {
		return ResponseEntity.ok(Map.of("url", "/poly/url1", "method", "method1()"));
	}
	
    /**
     * Endpoint requiring USER role.
     * @return A map containing the URL and method name.
     */
	@GetMapping("/poly/url2")
	public ResponseEntity<Map<String, String>> method2() {
		return ResponseEntity.ok(Map.of("url", "/poly/url2", "method", "method2()"));
	}
	
    /**
     * Endpoint requiring ADMIN role.
     * @return A map containing the URL and method name.
     */
	@GetMapping("/poly/url3")
	public ResponseEntity<Map<String, String>> method3() {
		return ResponseEntity.ok(Map.of("url", "/poly/url3", "method", "method3()"));
	}
	
    /**
     * Endpoint requiring either USER or ADMIN role.
     * @return A map containing the URL and method name.
     */
	@GetMapping("/poly/url4")
	public ResponseEntity<Map<String, String>> method4() {
		return ResponseEntity.ok(Map.of("url", "/poly/url4", "method", "method4()"));
	}
	
    /**
     * Endpoint to simulate an access denied scenario.
     * @return This method always throws an exception.
     */
	@GetMapping("/poly/access/denied")
	public ResponseEntity<Object> accessDenied() {
		throw new RuntimeException("Access Denied!");
	}
}
