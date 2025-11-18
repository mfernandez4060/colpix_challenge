package com.colpix.challenge.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;
import org.springframework.mock.web.*;

class TraceIdFilterTest {

    @AfterEach
    void cleanup() {
        MDC.clear();
    }

    @Test
    void addsTraceIdAndCleansUp() throws Exception {
        TraceIdFilter filter = new TraceIdFilter();

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, res, chain);

        String traceIdHeader = res.getHeader("X-Trace-Id");
        assertThat(traceIdHeader).isNotBlank();

        verify(chain).doFilter(req, res);

        assertThat(MDC.get("traceId")).isNull();
    }
}
