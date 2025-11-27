package com.colpix.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.colpix.challenge.entity.Employee;
import com.colpix.challenge.exception.BadRequestException;
import com.colpix.challenge.exception.InvalidCredentialsException;
import com.colpix.challenge.exception.NotFoundException;
import com.colpix.challenge.repository.EmployeeRepository;
import com.colpix.challenge.service.dto.EmployeeRequest;
import com.colpix.challenge.service.dto.EmployeeResponse;
import com.colpix.challenge.service.dto.PasswordUpdateRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository repo;
    @Mock private EmployeeHierarchyService hierarchy;
    @Mock private PasswordEncoder encoder;

    @InjectMocks private EmployeeService service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "passwordEncoder", encoder);
    }

    @Test
    void create_ok() {
        EmployeeRequest r = new EmployeeRequest();
        r.setUserName("john");
        r.setName("John Doe");
        r.setEmail("j@test.com");
        r.setPassword("1234");

        Employee saved = new Employee();
        saved.setId(1L);
        saved.setUpdatedAt(LocalDateTime.now());

        when(encoder.encode("1234")).thenReturn("ENC");
        when(repo.save(any())).thenReturn(saved);

        EmployeeResponse resp = service.create(r);

        assertThat(resp.getId()).isEqualTo(1L);
        verify(repo).save(any());
    }

    @Test
    void create_missingPassword_throws() {
        EmployeeRequest r = new EmployeeRequest();
        r.setUserName("john");
        r.setName("John");
        r.setEmail("j@t.com");

        assertThatThrownBy(() -> service.create(r))
            .isInstanceOf(BadRequestException.class);
    }


    @Test
    void getAll_mapsWell() {
        Employee e1 = new Employee();
        e1.setId(1L);
        e1.setName("A");
        e1.setEmail("a@e");
        e1.setUpdatedAt(LocalDateTime.now());

        Employee e2 = new Employee();
        e2.setId(2L);
        e2.setName("B");
        e2.setEmail("b@e");
        e2.setUpdatedAt(LocalDateTime.now());
        e2.setSupervisor(e1);

        when(repo.findAll()).thenReturn(List.of(e1, e2));

        var list = service.getAll();

        assertThat(list).hasSize(2);
        assertThat(list.get(1).getSupervisorId()).isEqualTo(1L);
    }

    @Test
    void getByIdWithSubordinates_ok() {
        Employee e = new Employee();
        e.setId(1L);
        e.setName("john");
        e.setEmail("mail");
        e.setUpdatedAt(LocalDateTime.now());

        when(repo.findById(1L)).thenReturn(Optional.of(e));
        when(hierarchy.countSubordinates(1L)).thenReturn(5);

        EmployeeResponse r = service.getByIdWithSubordinates(1L);

        assertThat(r.getSubordinatesCount()).isEqualTo(5);
    }

    @Test
    void getByIdWithSubordinates_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByIdWithSubordinates(1L))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    void updatePassword_ok() {
        Employee e = new Employee();
        e.setUserName("john");
        e.setPassword("ENC");

        PasswordUpdateRequest r = new PasswordUpdateRequest();
        r.setOldPassword("old");
        r.setNewPassword("new");

        when(repo.findByUserName("john")).thenReturn(Optional.of(e));
        when(encoder.matches("old", "ENC")).thenReturn(true);
        when(encoder.encode("new")).thenReturn("NEWENC");

        service.updatePassword("john", r);

        assertThat(e.getPassword()).isEqualTo("NEWENC");
        verify(repo).save(e);
    }

    @Test
    void updatePassword_wrongOld_throws() {
        Employee e = new Employee();
        e.setUserName("john");
        e.setPassword("ENC");

        PasswordUpdateRequest r = new PasswordUpdateRequest();
        r.setOldPassword("old");
        r.setNewPassword("new");

        when(repo.findByUserName("john")).thenReturn(Optional.of(e));
        when(encoder.matches("old", "ENC")).thenReturn(false);

        assertThatThrownBy(() -> service.updatePassword("john", r))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void updatePassword_userNotFound() {
        when(repo.findByUserName("john")).thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> service.updatePassword("john", new PasswordUpdateRequest()))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void update_missingId_throws() {
        EmployeeRequest r = new EmployeeRequest();

        assertThatThrownBy(() -> service.update(r))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    void update_withPassword_throws() {
        EmployeeRequest r = new EmployeeRequest();
        r.setId(1L);
        r.setPassword("shouldNot");

        assertThatThrownBy(() -> service.update(r))
            .isInstanceOf(BadRequestException.class);
    }

    @Test
    void update_ok() {
        Employee existing = new Employee();
        existing.setId(1L);
        existing.setName("old");
        existing.setEmail("old");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        EmployeeRequest r = new EmployeeRequest();
        r.setId(1L);
        r.setName("new");
        r.setEmail("mail");

        EmployeeResponse resp = service.update(r);

        assertThat(resp.getName()).isEqualTo("new");
        assertThat(resp.getEmail()).isEqualTo("mail");
    }
    
    @Test
    void delete_ok() {
        Employee e = new Employee();
        e.setId(10L);

        when(repo.findById(10L)).thenReturn(Optional.of(e));

        service.delete(10L);

        verify(repo).delete(e);
    }

    @Test
    void delete_notFound() {
        when(repo.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(10L))
            .isInstanceOf(NotFoundException.class);
    }

}
