package com.colpix.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
	private static final long serialVersionUID = 4601643493147361319L;

	public ConflictException(String message) { super(message); }
}
