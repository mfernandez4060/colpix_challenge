package com.colpix.challenge.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock private JwtUtils jwtUtils;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain chain;

    private JwtFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtFilter(jwtUtils);
        SecurityContextHolder.clearContext();
    }

    @Test
    void skip_when_login_or_swagger() {
        when(request.getServletPath()).thenReturn("/login");
        assertThat(filter.shouldNotFilter(request)).isTrue();

        when(request.getServletPath()).thenReturn("/swagger-ui/index.css");
        assertThat(filter.shouldNotFilter(request)).isTrue();

        when(request.getServletPath()).thenReturn("/employees");
        assertThat(filter.shouldNotFilter(request)).isFalse();
    }

    @Test
    void noAuthorizationHeader() throws Exception {
        when(request.getServletPath()).thenReturn("/employees");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain).doFilter(request, response);
    }

    @Test
    void validToken_setsAuthentication() throws Exception {
        String token = "valid.token";

        when(request.getServletPath()).thenReturn("/employees");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);

        when(jwtUtils.getUsernameFromToken(token)).thenReturn("john");
        when(jwtUtils.isTokenValid(token)).thenReturn(true);

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("john");

        verify(chain).doFilter(request, response);
    }
}
