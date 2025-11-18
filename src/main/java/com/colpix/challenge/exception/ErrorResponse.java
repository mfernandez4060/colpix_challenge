package com.colpix.challenge.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final String code;
    private final String message;
    private final LocalDateTime timestamp;
    private final String traceId;

    public ErrorResponse(String code, String message, String traceId) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.traceId = traceId;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getTraceId() { return traceId; }
}
