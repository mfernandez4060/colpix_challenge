package com.colpix.challenge.service;

import com.colpix.challenge.service.dto.LoginRequest;
import com.colpix.challenge.service.dto.LoginResponse;

public interface  IAuthService {
	LoginResponse login(LoginRequest request);
	void logout(String token);
}
