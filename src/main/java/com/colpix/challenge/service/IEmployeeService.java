package com.colpix.challenge.service;

import java.util.List;
import java.util.Optional;

import com.colpix.challenge.entity.Employee;
import com.colpix.challenge.service.dto.EmployeeRequest;
import com.colpix.challenge.service.dto.EmployeeResponse;
import com.colpix.challenge.service.dto.PasswordUpdateRequest;

public interface IEmployeeService {

	EmployeeResponse create(EmployeeRequest request);

	List<EmployeeResponse> getAll();

	EmployeeResponse getByIdWithSubordinates(Long id);

	void updatePassword(String username, PasswordUpdateRequest request);

	EmployeeResponse update(EmployeeRequest request);

	void delete(Long id);

	Optional<Employee> getByUserName(String username);

}