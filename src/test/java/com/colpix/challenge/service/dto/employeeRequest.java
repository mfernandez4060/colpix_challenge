package com.colpix.challenge.service.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class DtoTest {

    @Test
    void employeeRequest() {
        EmployeeRequest r = new EmployeeRequest();
        r.setId(1L);
        r.setUserName("john");
        r.setName("n");
        r.setEmail("e");
        r.setSupervisorId(2L);
        r.setPassword("p");

        assertThat(r.getId()).isEqualTo(1L);
        assertThat(r.toString()).contains("EmployeeRequest");
    }

    @Test
    void employeeResponse() {
        EmployeeResponse r =
            new EmployeeResponse(1L,"A","B",2L,null,3);

        assertThat(r.getSubordinatesCount()).isEqualTo(3);
    }

    @Test
    void loginRequest() {
        LoginRequest r = new LoginRequest();
        r.setUsername("a");
        r.setPassword("b");

        assertThat(r.getUsername()).isEqualTo("a");
    }

    @Test
    void loginResponse() {
        LoginResponse r = new LoginResponse("t",5);

        assertThat(r.getToken()).isEqualTo("t");
        assertThat(r.getExpiresIn()).isEqualTo(5);
    }

    @Test
    void passwordUpdateRequest() {
        PasswordUpdateRequest r = new PasswordUpdateRequest();
        r.setOldPassword("o");
        r.setNewPassword("n");

        assertThat(r.getOldPassword()).isEqualTo("o");
    }
}
