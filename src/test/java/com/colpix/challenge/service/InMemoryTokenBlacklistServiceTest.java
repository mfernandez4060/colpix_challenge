package com.colpix.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InMemoryTokenBlacklistServiceTest {

    @Test
    void testBlacklist() {
        InMemoryTokenBlacklistService s = new InMemoryTokenBlacklistService();

        assertThat(s.isInvalid("x")).isFalse();
        s.invalidate("x");
        assertThat(s.isInvalid("x")).isTrue();
    }
}
