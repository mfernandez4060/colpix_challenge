package com.colpix.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidCredentialsException extends RuntimeException {
	private static final long serialVersionUID = 3307235395021407274L;
	private static final String INVALID_CREDENTIALS = "Invalid credentials";

	public InvalidCredentialsException() {
		super(INVALID_CREDENTIALS);
	}
}
