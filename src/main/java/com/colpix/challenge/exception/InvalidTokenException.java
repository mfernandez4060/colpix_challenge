package com.colpix.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidTokenException extends RuntimeException {
	private static final String TOKEN_INVALIDATED = "Token invalidated";
	private static final long serialVersionUID = 3307235395021407274L;

	public InvalidTokenException() {
		super(TOKEN_INVALIDATED);
	}
}
