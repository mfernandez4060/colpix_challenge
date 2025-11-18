package com.colpix.challenge.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.colpix.challenge.entity.Employee;
import com.colpix.challenge.exception.BadRequestException;
import com.colpix.challenge.exception.InvalidCredentialsException;
import com.colpix.challenge.exception.NotFoundException;
import com.colpix.challenge.repository.EmployeeRepository;
import com.colpix.challenge.service.dto.EmployeeRequest;
import com.colpix.challenge.service.dto.EmployeeResponse;
import com.colpix.challenge.service.dto.PasswordUpdateRequest;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService implements IEmployeeService {
	private static final String UPDATE_EMPLOYEE_REQUEST_TRACE_ID = "Update Employee request={} traceId={}";
	private static final String UPDATE_PASSWORD_EMPLOYEE_USERNAME_TRACE_ID = "Update password Employee username={} traceId={}";
	private static final String GET_EMPLOYEE_ID_TRACE_ID = "Get Employee id={} traceId={}";
	private static final String GET_EMPLOYEES_TRACE_ID = "Get Employees traceId={}";
	private static final String CREATE_EMPLOYEE_REQUEST_TRACE_ID = "Create Employee request={} traceId={}";
	private static final String TRACE_ID = "traceId";
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);
    private static final String PASSWORD_CANNOT_BE_UPDATED_HERE = "Password cannot be updated here";
	private static final String EMPLOYEE_ID_IS_REQUIRED_FOR_UPDATE = "Employee ID is required for update";
	private static final String SUPERVISOR_NOT_FOUND = "Supervisor not found";
	private static final String PASSWORD_NOT_FOUND = "Password not found";
	private static final String EMPLOYEE_NOT_FOUND = "Employee not found";
	private final EmployeeRepository employeeRepository;
    private final EmployeeHierarchyService hierarchyService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeHierarchyService hierarchyService) {
        this.employeeRepository = employeeRepository;
        this.hierarchyService = hierarchyService;
    }

    @Override
	@Transactional
    public EmployeeResponse create(EmployeeRequest request) {
	    String traceId = MDC.get(TRACE_ID);
	    LOG.info(CREATE_EMPLOYEE_REQUEST_TRACE_ID, request.toString(), traceId);
	    
        Employee employee;
        if (request.getId() != null) {
            employee = employeeRepository.findById(request.getId())
                    .orElseThrow(() -> new BadRequestException(EMPLOYEE_NOT_FOUND));
        } else {
            employee = new Employee();
        }
        employee.setUserName(request.getUserName());
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        
        if (request.getPassword() == null) {
        	throw new BadRequestException(PASSWORD_NOT_FOUND);
        }
        
	    employee.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getSupervisorId() != null) {
            Employee supervisor = employeeRepository.findById(request.getSupervisorId())
                    .orElseThrow(() -> new BadRequestException(SUPERVISOR_NOT_FOUND));
            employee.setSupervisor(supervisor);
        } else {
            employee.setSupervisor(null);
        }

        Employee saved = employeeRepository.save(employee);
        Long supervisorId = saved.getSupervisor() != null ? saved.getSupervisor().getId() : null;

        return new EmployeeResponse(saved.getId(), saved.getName(), saved.getEmail(),
                supervisorId, saved.getUpdatedAt(), null);
    }

    @Override
	@Transactional(readOnly = true)
    public List<EmployeeResponse> getAll() {
	    String traceId = MDC.get(TRACE_ID);
	    LOG.info(GET_EMPLOYEES_TRACE_ID, traceId);
    	
        return employeeRepository.findAll().stream()
                .map(e -> new EmployeeResponse(
                        e.getId(),
                        e.getName(),
                        e.getEmail(),
                        e.getSupervisor() != null ? e.getSupervisor().getId() : null,
                        e.getUpdatedAt(),
                        null
                )).collect(Collectors.toList());
    }

    @Override
	public EmployeeResponse getByIdWithSubordinates(Long id) {
	    String traceId = MDC.get(TRACE_ID);
	    LOG.info(GET_EMPLOYEE_ID_TRACE_ID, id, traceId);
	    
        CompletableFuture<Optional<Employee>> employeeFuture =
                CompletableFuture.supplyAsync(() -> employeeRepository.findById(id));

        CompletableFuture<Integer> subordinatesFuture =
                CompletableFuture.supplyAsync(() -> hierarchyService.countSubordinates(id));

        CompletableFuture.allOf(employeeFuture, subordinatesFuture).join();

        Employee employee = employeeFuture.join()
                .orElseThrow(() -> new BadRequestException(EMPLOYEE_NOT_FOUND));

        int subordinatesCount = subordinatesFuture.join();

        Long supervisorId = employee.getSupervisor() != null ? employee.getSupervisor().getId() : null;

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                supervisorId,
                employee.getUpdatedAt(),
                subordinatesCount
        );
    }

	@Override
	@Transactional
	public void updatePassword(String username, PasswordUpdateRequest request) {
	    String traceId = MDC.get(TRACE_ID);
	    LOG.info(UPDATE_PASSWORD_EMPLOYEE_USERNAME_TRACE_ID, username, traceId);
	    
	    // Buscar empleado por email/username
	    Employee employee = employeeRepository.findByUserName(username)
	            .orElseThrow(() -> new InvalidCredentialsException());

	    // Validar contraseña actual
	    if (!checkPassword(employee, request.getOldPassword())) {
	        throw new InvalidCredentialsException();
	    }

	    // Actualizar contraseña
	    employee.setPassword(passwordEncoder.encode(request.getNewPassword()));

	    // Persistir cambios
	    Employee saved = employeeRepository.save(employee);
	}

	@Override
	@Transactional
    public EmployeeResponse update(EmployeeRequest request) {
	    String traceId = MDC.get(TRACE_ID);
	    LOG.info(UPDATE_EMPLOYEE_REQUEST_TRACE_ID, request.toString(), traceId);
        Employee employee;
        if (request.getId() != null) {
            employee = employeeRepository.findById(request.getId())
                    .orElseThrow(() -> new BadRequestException(EMPLOYEE_NOT_FOUND));
        } else {
			throw new BadRequestException(EMPLOYEE_ID_IS_REQUIRED_FOR_UPDATE);
		}
        if (request.getPassword() != null) {
			throw new BadRequestException(PASSWORD_CANNOT_BE_UPDATED_HERE);
		}
        
        if (request.getUserName() != null) {
			employee.setUserName(request.getUserName());
		}
        if (request.getEmail() != null) {
        	employee.setEmail(request.getEmail());
        }
        if (request.getName() != null) {
        	employee.setName(request.getName());
        }
        
        if (request.getSupervisorId() != null) {
            Employee supervisor = employeeRepository.findById(request.getSupervisorId())
                    .orElseThrow(() -> new BadRequestException(SUPERVISOR_NOT_FOUND));
            employee.setSupervisor(supervisor);
        } else {
            employee.setSupervisor(null);
        }

        Employee saved = employeeRepository.save(employee);
        Long supervisorId = saved.getSupervisor() != null ? saved.getSupervisor().getId() : null;

        return new EmployeeResponse(saved.getId(), saved.getName(), saved.getEmail(),
                supervisorId, saved.getUpdatedAt(), null);
    }

	@Override
	@Transactional
	public void delete(Long id) {
		Employee employee = employeeRepository.findById(id)
		        .orElseThrow(() -> new NotFoundException("Employee not found: " + id));

		    employeeRepository.delete(employee);
	}

	@Override
	public Optional<Employee> getByUserName(String username) {
		return employeeRepository.findByUserName(username);
	}

	public boolean checkPassword(Employee user, String password) {
		if (passwordEncoder.matches(password, user.getPassword())) {
			return true;
		}
		return false;
	}

}
