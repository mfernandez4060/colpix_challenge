package com.colpix.challenge.config;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.colpix.challenge.service.TokenBlacklistService;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private TokenBlacklistService blacklistService;

    @BeforeEach
    void setup() {
        jwtUtils = new JwtUtils();
        blacklistService = mock(TokenBlacklistService.class);

        ReflectionTestUtils.setField(jwtUtils, "tokenBlacklistService", blacklistService);

        String rawKey = "my-secret-key-that-is-very-long-for-jwt";
        String encodedKey = Base64.getEncoder().encodeToString(rawKey.getBytes());

        ReflectionTestUtils.setField(jwtUtils, "secret", encodedKey);
        ReflectionTestUtils.setField(jwtUtils, "expirationInMs", 300000L);
    }

    @Test
    void generateAndValidateToken_success() {
        String token = jwtUtils.generateToken("john");

        assertThat(token).isNotNull();

        when(blacklistService.isInvalid(token)).thenReturn(false);

        String username = jwtUtils.getUsernameFromToken(token);
        assertThat(username).isEqualTo("john");

        assertThat(jwtUtils.isTokenValid(token)).isTrue();
    }

    @Test
    void invalidatedToken_returnsFalse() {
        String token = jwtUtils.generateToken("john");

        when(blacklistService.isInvalid(token)).thenReturn(true);

        assertThat(jwtUtils.isTokenValid(token)).isFalse();
    }
}
