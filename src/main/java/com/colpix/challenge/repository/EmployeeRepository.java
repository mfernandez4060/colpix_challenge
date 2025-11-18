package com.colpix.challenge.repository;

import com.colpix.challenge.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findBySupervisor_Id(Long supervisorId);

	Optional<Employee> findByUserName(String username);
}
