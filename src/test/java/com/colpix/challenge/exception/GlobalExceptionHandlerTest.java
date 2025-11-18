package com.colpix.challenge.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.*;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @AfterEach
    void clean() {
        MDC.clear();
    }

    @Test
    void runtimeException_returnsMap() {
        RuntimeException ex = new RuntimeException("fail");

        ResponseEntity<Map<String,Object>> r = handler.handleRuntimeException(ex);

        assertThat(r.getBody().get("error")).isEqualTo("fail");
    }

    @Test
    void authenticationException_createsResponse() {
        MDC.put("traceId", "t1");

        AuthenticationException ex = new AuthenticationException("auth");

        ResponseEntity<ErrorResponse> r = handler.handleAuth(ex);

        assertThat(r.getBody().getCode()).isEqualTo("AUTH_ERROR");
        assertThat(r.getBody().getTraceId()).isEqualTo("t1");
    }

    @Test
    void badRequestException_createsResponse() {
        MDC.put("traceId", "xyz");

        BadRequestException ex = new BadRequestException("bad");

        ResponseEntity<ErrorResponse> r = handler.handleNotFound(ex);

        assertThat(r.getBody().getCode()).isEqualTo("NOT FOUND ERROR");
        assertThat(r.getBody().getTraceId()).isEqualTo("xyz");
    }
}
