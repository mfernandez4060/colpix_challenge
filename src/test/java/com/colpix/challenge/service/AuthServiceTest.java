package com.colpix.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.colpix.challenge.config.JwtUtils;
import com.colpix.challenge.entity.Employee;
import com.colpix.challenge.exception.InvalidCredentialsException;
import com.colpix.challenge.service.dto.LoginRequest;
import com.colpix.challenge.service.dto.LoginResponse;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private JwtUtils jwtUtils;
    @Mock private TokenBlacklistService blacklist;
    @Mock private EmployeeService employeeService;

    @InjectMocks private AuthService service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "expirationInMs", 300000L);
    }

    @Test
    void adminUser_success() {
        LoginRequest req = new LoginRequest();
        req.setUsername("admin");
        req.setPassword("password");

        when(jwtUtils.generateToken("admin")).thenReturn("jwt-token");

        LoginResponse resp = service.login(req);

        assertThat(resp.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void normalUser_success() {
        LoginRequest req = new LoginRequest();
        req.setUsername("john");
        req.setPassword("1234");

        Employee e = new Employee();
        e.setUserName("john");

        when(employeeService.getByUserName("john")).thenReturn(Optional.of(e));
        when(employeeService.checkPassword(e, "1234")).thenReturn(true);
        when(jwtUtils.generateToken("john")).thenReturn("token-john");

        LoginResponse resp = service.login(req);

        assertThat(resp.getToken()).isEqualTo("token-john");
    }

    @Test
    void login_userNotFound_throws() {
        LoginRequest req = new LoginRequest();
        req.setUsername("john");
        req.setPassword("1234");

        when(employeeService.getByUserName("john")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(req))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void logout_callsBlacklist() {
        service.logout("abc123");

        verify(blacklist).invalidate("abc123");
    }
}
