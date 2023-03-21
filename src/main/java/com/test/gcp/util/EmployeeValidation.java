package com.test.gcp.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.test.gcp.exception.ResourceFoundException;
import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.DepartmentDTO;
import com.test.gcp.payload.EmployeeDTO;
import com.test.gcp.service.DepartmentService;
import com.test.gcp.service.EmployeeService;

@Component
public class EmployeeValidation {

    public void validateAddEmployee(final EmployeeDTO employeeDTO) {

        Optional<DepartmentDTO> presentDepartmentDTO = DepartmentService.DEPARTMENTS.stream().filter(e -> e.getDepartmentId().equals(employeeDTO.getDepartmentId())).findFirst();
        if (!presentDepartmentDTO.isPresent()) {
            throw new ResourceNotFoundException("departmentId", employeeDTO.getDepartmentId());
        } else {
            employeeDTO.setDepartmentName(presentDepartmentDTO.get().getDepartmentName());
            Optional<EmployeeDTO> optionalEmployeeDTO = EmployeeService.EMPLOYEES.stream().filter(e -> e.getEmail().equalsIgnoreCase(employeeDTO.getEmail())).findFirst();
            if (optionalEmployeeDTO.isPresent()) {
                throw new ResourceFoundException("Email", employeeDTO.getEmail());
            }
        }
    }

    public EmployeeDTO validateUpdateEmployee(final EmployeeDTO employeeDTO, final String employeeId) {

        EmployeeDTO employeeDTO2 = null;
        Optional<DepartmentDTO> presentDepartmentDTO = DepartmentService.DEPARTMENTS.stream().filter(e -> e.getDepartmentId().equals(employeeDTO.getDepartmentId())).findFirst();
        if (!presentDepartmentDTO.isPresent()) {
            throw new ResourceNotFoundException("departmentId", employeeDTO.getDepartmentId());
        } else {
            Optional<EmployeeDTO> presentEmployeeDTO = EmployeeService.EMPLOYEES.stream().filter(e -> e.getEmployeeId().equals(employeeId)).findFirst();
            if (!presentEmployeeDTO.isPresent()) {
                throw new ResourceNotFoundException("employeeId", employeeId);
            } else {
                Optional<EmployeeDTO> optionalEmployeeDTO = EmployeeService.EMPLOYEES.stream().filter(e -> e.getEmail().equalsIgnoreCase(employeeDTO.getEmail()) && !e.getEmployeeId().equals(employeeId)).findFirst();
                if (optionalEmployeeDTO.isPresent()) {
                    throw new ResourceFoundException("Email", employeeDTO.getEmail());
                }
            }
            employeeDTO2 = presentEmployeeDTO.get();
            employeeDTO2.setDepartmentName(presentDepartmentDTO.get().getDepartmentName());
        }
        return employeeDTO2;
    }
}
