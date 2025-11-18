package com.colpix.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.colpix.challenge.config.JwtUtils;
import com.colpix.challenge.entity.Employee;
import com.colpix.challenge.exception.InvalidCredentialsException;
import com.colpix.challenge.service.dto.LoginRequest;
import com.colpix.challenge.service.dto.LoginResponse;

@Service
public class AuthService implements IAuthService {
	private static final String TRACE_ID = "traceId";

	private static final String LOGOUT_TOKEN_TRACE_ID = "Logout token={} traceId={}";

	private static final String LOGIN_SUCCESSFUL_USER_NAME_TRACE_ID = "Login successful userName={} traceId={}";

	private static final String LOGIN_FAILED_INVALID_PASSWORD_USER_NAME_TRACE_ID = "Login failed: invalid password userName={} traceId={}";

	private static final String LOGIN_ATTEMPT_USER_NAME_TRACE_ID = "Login attempt userName={} traceId={}";

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);	

	private static final String PASSWORD = "password";
	private static final String ADMIN = "admin";
	private static final String $_JWT_EXPIRATION = "${jwt.expiration}";
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private TokenBlacklistService tokenBlacklistService;
	 
	@Autowired
	private EmployeeService employeeService; 

	@Value($_JWT_EXPIRATION)
	private long expirationInMs;

	public LoginResponse login(LoginRequest request) {
	    String traceId = MDC.get(TRACE_ID);
	    LOG.info(LOGIN_ATTEMPT_USER_NAME_TRACE_ID, request.getUsername(), traceId);
	    
		// Simple hardcoded user for challenge purposes
		if (!ADMIN.equals(request.getUsername()) || !PASSWORD.equals(request.getPassword())) {
			Employee user = employeeService.getByUserName(request.getUsername())
				.orElseThrow(() -> new InvalidCredentialsException());
			
			if (!employeeService.checkPassword(user, request.getPassword())) {
				LOG.warn(LOGIN_FAILED_INVALID_PASSWORD_USER_NAME_TRACE_ID, request.getUsername(), traceId);
				throw new InvalidCredentialsException();
			}
		}
		
		LOG.info(LOGIN_SUCCESSFUL_USER_NAME_TRACE_ID, request.getUsername(), traceId);
		 
		String token = jwtUtils.generateToken(request.getUsername());
		return new LoginResponse(token, expirationInMs / 1000);
	}

	@Override
	public void logout(String token) {
	    String traceId = MDC.get(TRACE_ID);
	    LOG.info(LOGOUT_TOKEN_TRACE_ID, token, traceId);
		tokenBlacklistService.invalidate(token);
	}

}
