package com.colpix.challenge.service;

import com.colpix.challenge.entity.Employee;
import com.colpix.challenge.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeHierarchyService {

    private final EmployeeRepository employeeRepository;

    public EmployeeHierarchyService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public int countSubordinates(Long employeeId) {
        List<Employee> directReports = employeeRepository.findBySupervisor_Id(employeeId);
        int total = 0;
        for (Employee e : directReports) {
            total += countSubordinates(e.getId());
            if (e.getSupervisor() != null && e.getSupervisor().getId().equals(employeeId)) {
				total += 1;
			}
        }
        return total;
    }
}
