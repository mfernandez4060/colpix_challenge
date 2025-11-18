package com.colpix.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.colpix.challenge.service.AuthService;
import com.colpix.challenge.service.dto.LoginRequest;
import com.colpix.challenge.service.dto.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class AuthController {

	private static final String BEARER = "Bearer ";
	private static final String AUTHORIZATION = "Authorization";
	private static final String INVALIDATES_CURRENT_JWT = "Invalidates current JWT";
	private static final String USER_LOGOUT = "User logout";
	private static final String LOGOUT = "/logout";
	private static final String LOGIN = "/login";
	private static final String RETURNS_A_JWT_VALID_FOR_5_MINUTES = "Returns a JWT valid for 5 minutes";
	private static final String USER_LOGIN = "User login";
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = USER_LOGIN, description = RETURNS_A_JWT_VALID_FOR_5_MINUTES)
	@PostMapping(LOGIN)
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}
	
	@DeleteMapping(LOGOUT)
	@Operation(summary = USER_LOGOUT, description = INVALIDATES_CURRENT_JWT)
	public ResponseEntity<Void> logout(HttpServletRequest request) {
	    String authHeader = request.getHeader(AUTHORIZATION);

	    if (authHeader != null && authHeader.startsWith(BEARER)) {
	        String token = authHeader.substring(7);
	        authService.logout(token);
	    }

	    return ResponseEntity.ok().build();
	}	

}
