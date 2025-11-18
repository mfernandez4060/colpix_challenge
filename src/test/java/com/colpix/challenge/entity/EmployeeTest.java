package com.colpix.challenge.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void testEntity() {
        Employee sup = new Employee();
        sup.setId(10L);

        Employee e = new Employee();
        e.setId(1L);
        e.setUserName("john");
        e.setName("John");
        e.setEmail("x@y.z");
        e.setPassword("pwd");
        e.setSupervisor(sup);
        e.setUpdatedAt(LocalDateTime.now());

        assertThat(e.getId()).isEqualTo(1L);
        assertThat(e.getSupervisor()).isSameAs(sup);

        e.onUpdate();
        assertThat(e.getUpdatedAt()).isNotNull();
    }
}
