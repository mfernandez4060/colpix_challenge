package com.colpix.challenge.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String AUTH_ERROR = "AUTH_ERROR";
	private static final String NOT_FOUND_ERROR = "NOT FOUND ERROR";
	private static final String ERROR = "error";
	private static final String TIMESTAMP = "timestamp";

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put(TIMESTAMP, LocalDateTime.now().toString());
		body.put(ERROR, ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex) {
	    return new ResponseEntity<>(
	        build(AUTH_ERROR, ex.getMessage()),
	        HttpStatus.UNAUTHORIZED
	    );
	}
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(BadRequestException ex) {
	    return new ResponseEntity<>(
		        build(NOT_FOUND_ERROR, ex.getMessage()),
		        HttpStatus.NOT_FOUND
		    );
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

	    Map<String, Object> body = new HashMap<>();
	    body.put("timestamp", LocalDateTime.now().toString());

	    Map<String, String> errors = new HashMap<>();

	    ex.getBindingResult().getFieldErrors().forEach(error ->
	        errors.put(error.getField(), error.getDefaultMessage())
	    );

	    body.put("errors", errors);

	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	private ErrorResponse build(String code, String message) {
	    return new ErrorResponse(code, message, MDC.get("traceId"));
	}


}
