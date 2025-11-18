package com.colpix.challenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colpix.challenge.service.IEmployeeService;
import com.colpix.challenge.service.dto.EmployeeRequest;
import com.colpix.challenge.service.dto.EmployeeResponse;
import com.colpix.challenge.service.dto.PasswordUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private static final String DELETES_AN_EXISTING_EMPLOYEE_IDENTIFIED_BY_ITS_ID_IF_THE_EMPLOYEE_DOES_NOT_EXIST_A_404_NOT_FOUND_IS_RETURNED = "Deletes an existing employee identified by its ID. \nIf the employee does not exist, a 404 Not Found is returned.";
	private static final String DELETE_AN_EMPLOYEE = "Delete an employee";
	private static final String PASSWORD = "/password";
	private static final String ALLOWS_THE_CURRENTLY_AUTHENTICATED_USER_TO_UPDATE_THEIR_PASSWORD_THE_USERNAME_IS_EXTRACTED_FROM_THE_JWT_TOKEN_NO_USERNAME_IS_REQUIRED_IN_THE_REQUEST_BODY = "Allows the currently authenticated user to update their password.\nThe username is extracted from the JWT token; no username is required in the request body.";
	private static final String UPDATE_THE_AUTHENTICATED_USER_S_PASSWORD = "Update the authenticated user's password";
	private static final String RETRIEVES_DETAILED_INFORMATION_ABOUT_AN_EMPLOYEE_INCLUDING_THEIR_PERSONAL_INFORMATION_SUPERVISOR_LAST_UPDATE_TIMESTAMP_AND_THE_NUMBER_OF_SUBORDINATES_DIRECT_AND_INDIRECT_THE_EMPLOYEE_RETRIEVAL_AND_SUBORDINATE_COUNTING_ARE_EXECUTED_IN_PARALLEL_FOR_OPTIMIZED_PERFORMANCE = "Retrieves detailed information about an employee, including their personal information,\nsupervisor, last update timestamp, and the number of subordinates (direct and indirect).\nThe employee retrieval and subordinate counting are executed in parallel for optimized performance.";
	private static final String GET_DETAILED_EMPLOYEE_INFORMATION_BY_ID = "Get detailed employee information by ID";
	private static final String RETURNS_A_LIST_OF_ALL_EMPLOYEES_IN_THE_SYSTEM_INCLUDING_THEIR_BASIC_DETAILS_AND_THE_TIMESTAMP_OF_THE_LAST_UPDATE_THIS_ENDPOINT_DOES_NOT_CALCULATE_SUBORDINATES = "Returns a list of all employees in the system, including their basic details and the\ntimestamp of the last update. This endpoint does not calculate subordinates.";
	private static final String GET_ALL_EMPLOYEES = "Get all employees";
	private static final String ID2 = "id";
	private static final String ID = "/{id}";
	private static final String UPDATES_AN_EXISTING_EMPLOYEE_IDENTIFIED_BY_ITS_ID_THE_PAYLOAD_MUST_INCLUDE_VALID_EMPLOYEE_ATTRIBUTES_THE_RESPONSE_RETURNS_THE_UPDATED_EMPLOYEE_WITH_ITS_LAST_UPDATE_TIMESTAMP = "Updates an existing employee identified by its ID.\n The payload must include valid employee attributes.\nThe response returns the updated employee with its last update timestamp.";
	private static final String UPDATE_AN_EMPLOYEE = "Update an employee";
	private static final String CREATES_A_NEW_EMPLOYEE_OR_UPDATES_AN_EXISTING_ONE_BASED_ON_THE_PROVIDED_JSON_PAYLOAD_IF_AN_ID_IS_INCLUDED_THE_EXISTING_RECORD_IS_UPDATED_OTHERWISE_A_NEW_EMPLOYEE_IS_CREATED_THE_RESPONSE_INCLUDES_THE_PERSISTED_EMPLOYEE_DATA_AND_THE_TIMESTAMP_OF_THE_LAST_UPDATE = "Creates a new employee or updates an existing one based on the provided JSON payload.\nIf an ID is included, the existing record is updated; otherwise, a new employee is created.\nThe response includes the persisted employee data and the timestamp of the last update.";
	private static final String CREATE_OR_UPDATE_AN_EMPLOYEE = "Create or update an employee";
	private final IEmployeeService employeeService;

	public EmployeeController(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Operation(summary = CREATE_OR_UPDATE_AN_EMPLOYEE, description = CREATES_A_NEW_EMPLOYEE_OR_UPDATES_AN_EXISTING_ONE_BASED_ON_THE_PROVIDED_JSON_PAYLOAD_IF_AN_ID_IS_INCLUDED_THE_EXISTING_RECORD_IS_UPDATED_OTHERWISE_A_NEW_EMPLOYEE_IS_CREATED_THE_RESPONSE_INCLUDES_THE_PERSISTED_EMPLOYEE_DATA_AND_THE_TIMESTAMP_OF_THE_LAST_UPDATE)
	@PostMapping
	public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
		return ResponseEntity.ok(employeeService.create(request));
	}

	@Operation(summary = UPDATE_AN_EMPLOYEE, description = UPDATES_AN_EXISTING_EMPLOYEE_IDENTIFIED_BY_ITS_ID_THE_PAYLOAD_MUST_INCLUDE_VALID_EMPLOYEE_ATTRIBUTES_THE_RESPONSE_RETURNS_THE_UPDATED_EMPLOYEE_WITH_ITS_LAST_UPDATE_TIMESTAMP)
	@PutMapping(ID)
	public ResponseEntity<EmployeeResponse> update(@PathVariable(ID2) Long id,
			@Valid @RequestBody EmployeeRequest request) {
		request.setId(id);
		return ResponseEntity.ok(employeeService.update(request));
	}

	@Operation(summary = GET_ALL_EMPLOYEES, description = RETURNS_A_LIST_OF_ALL_EMPLOYEES_IN_THE_SYSTEM_INCLUDING_THEIR_BASIC_DETAILS_AND_THE_TIMESTAMP_OF_THE_LAST_UPDATE_THIS_ENDPOINT_DOES_NOT_CALCULATE_SUBORDINATES)
	@GetMapping
	public ResponseEntity<List<EmployeeResponse>> getAll() {
		return ResponseEntity.ok(employeeService.getAll());
	}

	@Operation(summary = GET_DETAILED_EMPLOYEE_INFORMATION_BY_ID, description = RETRIEVES_DETAILED_INFORMATION_ABOUT_AN_EMPLOYEE_INCLUDING_THEIR_PERSONAL_INFORMATION_SUPERVISOR_LAST_UPDATE_TIMESTAMP_AND_THE_NUMBER_OF_SUBORDINATES_DIRECT_AND_INDIRECT_THE_EMPLOYEE_RETRIEVAL_AND_SUBORDINATE_COUNTING_ARE_EXECUTED_IN_PARALLEL_FOR_OPTIMIZED_PERFORMANCE)
	@GetMapping(ID)
	public ResponseEntity<EmployeeResponse> getById(@PathVariable(ID2) Long id) {
		return ResponseEntity.ok(employeeService.getByIdWithSubordinates(id));
	}

	@Operation(summary = UPDATE_THE_AUTHENTICATED_USER_S_PASSWORD, description = ALLOWS_THE_CURRENTLY_AUTHENTICATED_USER_TO_UPDATE_THEIR_PASSWORD_THE_USERNAME_IS_EXTRACTED_FROM_THE_JWT_TOKEN_NO_USERNAME_IS_REQUIRED_IN_THE_REQUEST_BODY)
	@PutMapping(PASSWORD)
	public ResponseEntity<Void> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		employeeService.updatePassword(username, request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = DELETE_AN_EMPLOYEE, description = DELETES_AN_EXISTING_EMPLOYEE_IDENTIFIED_BY_ITS_ID_IF_THE_EMPLOYEE_DOES_NOT_EXIST_A_404_NOT_FOUND_IS_RETURNED)
	@DeleteMapping(ID)
	public ResponseEntity<Void> delete(@PathVariable(ID2) Long id) {
		employeeService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
