package com.colpix.challenge.exception;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class CustomExceptionsTest {

    @Test void authEx() {
        AuthenticationException ex = new AuthenticationException("x");
        assertThat(ex.getMessage()).isEqualTo("x");
    }

    @Test void badEx() {
        BadRequestException ex = new BadRequestException("y");
        assertThat(ex.getMessage()).isEqualTo("y");
    }

    @Test void confEx() {
        ConflictException ex = new ConflictException("z");
        assertThat(ex.getMessage()).isEqualTo("z");
    }

    @Test void invalidCredEx() {
        InvalidCredentialsException ex = new InvalidCredentialsException();
        assertThat(ex.getMessage()).isEqualTo("Invalid credentials");
    }

    @Test void errorResponse() {
        ErrorResponse er = new ErrorResponse("C","M","T");
        assertThat(er.getCode()).isEqualTo("C");
        assertThat(er.getMessage()).isEqualTo("M");
        assertThat(er.getTraceId()).isEqualTo("T");
        assertThat(er.getTimestamp()).isNotNull();
    }
}
