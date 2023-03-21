package com.test.gcp.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.gcp.exception.ResourceFoundException;
import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.DepartmentDTO;
import com.test.gcp.payload.EmployeeDTO;
import com.test.gcp.service.DepartmentService;
import com.test.gcp.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeValidationTest {

    @InjectMocks
    private EmployeeValidation employeeValidation;

    private EmployeeDTO employeeDTO;
    private EmployeeDTO employeeDTO2;
    private List<EmployeeDTO> employeeDTOs;
    private DepartmentDTO departmentDTO;
    private DepartmentDTO departmentDTO2;
    private List<DepartmentDTO> departmentDTOs;

    @BeforeEach
    void setUp() throws Exception {
        departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId("1");
        departmentDTO.setDepartmentName("FS");

        departmentDTO2 = new DepartmentDTO();
        departmentDTO2.setDepartmentId("2");
        departmentDTO2.setDepartmentName("NON-FS");

        departmentDTOs = new ArrayList<>();
        departmentDTOs.add(departmentDTO2);
        departmentDTOs.add(departmentDTO);
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId("1");
        employeeDTO.setName("Avirup");
        employeeDTO.setEmail("avirup@gmail.com");
        employeeDTO.setDepartmentId("1");

        employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setEmployeeId("2");
        employeeDTO2.setName("Sumit");
        employeeDTO2.setEmail("sumit@gmail.com");
        employeeDTO2.setDepartmentId("2");

        employeeDTOs = new ArrayList<>();
        employeeDTOs.add(employeeDTO2);
        employeeDTOs.add(employeeDTO);

        EmployeeService.EMPLOYEES.clear();
    }

    @Test @Order(1)
    void testValidateAddEmployee() {
        employeeValidation.validateAddEmployee(employeeDTO);
    }

    @Test @Order(2)
    void testValidateAddEmployee_ResourceFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        Throwable throwable = assertThrows(ResourceFoundException.class, () -> employeeValidation.validateAddEmployee(employeeDTO));
        assertEquals("Email already found with : 'avirup@gmail.com'", throwable.getMessage());
    }

    @Test @Order(3)
    void testValidateAddEmployee_ResourceNotFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        employeeDTO.setDepartmentId("3");
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> employeeValidation.validateAddEmployee(employeeDTO));
        assertEquals("departmentId not found with : '3'", throwable.getMessage());
    }

    @Test @Order(4)
    void testValidateUpdateEmployee() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        employeeValidation.validateUpdateEmployee(employeeDTO, "1");
    }

    @Test @Order(5)
    void testValidateUpdateEmployee_ResourceNotFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> employeeValidation.validateUpdateEmployee(employeeDTO, "3"));
        assertEquals("employeeId not found with : '3'", throwable.getMessage());
    }

    @Test @Order(6)
    void testValidateUpdateEmployee_DepartmentResourceNotFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        employeeDTO.setDepartmentId("3");
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> employeeValidation.validateUpdateEmployee(employeeDTO, "3"));
        assertEquals("departmentId not found with : '3'", throwable.getMessage());
    }

    @Test @Order(7)
    void testValidateUpdateEmployee_ResourceFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        Throwable throwable = assertThrows(ResourceFoundException.class, () -> employeeValidation.validateUpdateEmployee(employeeDTO, "2"));
        assertEquals("Email already found with : 'avirup@gmail.com'", throwable.getMessage());
    }
}
