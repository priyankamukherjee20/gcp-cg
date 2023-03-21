package com.test.gcp.service;

import java.util.LinkedList;
import java.util.List;

import com.test.gcp.payload.AdminDTO;
import com.test.gcp.payload.EmployeeDTO;

public interface EmployeeService {

    List<EmployeeDTO> EMPLOYEES = new LinkedList<>();
    List<AdminDTO> ADMINS = new LinkedList<>();

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    List<EmployeeDTO> getEmployees();

    EmployeeDTO getEmployeesById(String employeeId);

    EmployeeDTO updateEmployee(String employeeId, EmployeeDTO employeeDTO);

    void deleteEmployee(String employeeId);

    List<EmployeeDTO> getEmployeesByDepartmentId(String departmentId);
}
