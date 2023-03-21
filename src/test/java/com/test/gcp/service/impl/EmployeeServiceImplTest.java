package com.test.gcp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.gcp.JsonReader;
import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.DepartmentDTO;
import com.test.gcp.payload.EmployeeDTO;
import com.test.gcp.service.DepartmentService;
import com.test.gcp.service.EmployeeService;
import com.test.gcp.util.EmployeeValidation;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @Mock
    private EmployeeValidation employeeValidation;

    private EmployeeDTO employeeDTO;
    private EmployeeDTO employeeDTO2;
    private List<EmployeeDTO> employeeDTOs;

    @BeforeEach
    void setUp() throws Exception {
        JsonReader<EmployeeDTO> mapper = new JsonReader<EmployeeDTO>(EmployeeDTO.class);
        employeeDTO = (EmployeeDTO) mapper.loadTestJson("test-resources/employee/employee.json");
        employeeDTO2 = (EmployeeDTO) mapper.loadTestJson("test-resources/employee/employee2.json");

        employeeDTOs = new ArrayList<>();
        employeeDTOs.add(employeeDTO2);
        employeeDTOs.add(employeeDTO);
    }

    @Test
    void testCreateEmployee() {
        Mockito.doNothing().when(employeeValidation).validateAddEmployee(employeeDTO);
        EmployeeDTO actualResponse = employeeServiceImpl.createEmployee(employeeDTO);
        assertEquals(String.valueOf(EmployeeService.EMPLOYEES.size()), actualResponse.getEmployeeId());
    }

    @Test
    void testGetEmployees() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        assertEquals(EmployeeService.EMPLOYEES.size(), employeeServiceImpl.getEmployees().size());
    }

    @Test
    void testGetEmployeesByDepartmentId() {
        DepartmentDTO departmentDTO;
        DepartmentDTO departmentDTO2;
        List<DepartmentDTO> departmentDTOs;
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
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        assertEquals(EmployeeService.EMPLOYEES.stream().filter(e -> e.getDepartmentId().equals("1")).count(), employeeServiceImpl.getEmployeesByDepartmentId("1").size());
    }

    @Test
    void testGetEmployeesByDepartmentId_ResourceFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getEmployeesByDepartmentId("20"));
        assertEquals("departmentId not found with : '20'", throwable.getMessage());
    }

    @Test
    void testGetEmployeesById() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        EmployeeDTO actualResponse = employeeServiceImpl.getEmployeesById("1");
        assertEquals("1", actualResponse.getEmployeeId());
    }

    @Test
    void testGetDepartmentsById_ResourceFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getEmployeesById("20"));
        assertEquals("employeeId not found with : '20'", throwable.getMessage());
    }

    @Test
    void testUpdateEmployee() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        EmployeeDTO actualResponse = employeeServiceImpl.updateEmployee("1", employeeDTO);
        assertEquals("1", actualResponse.getEmployeeId());
    }

    @Test
    void testDeleteEmployee() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        employeeServiceImpl.deleteEmployee("2");
        // Mockito.verify(EmployeeService.EMPLOYEES.remove(employeeDTO),
        // Mockito.times(1));
    }

    @Test
    void testDeleteDepartment_ResourceFoundException() {
        EmployeeService.EMPLOYEES.addAll(employeeDTOs);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.deleteEmployee("20"));
        assertEquals("employeeId not found with : '20'", throwable.getMessage());
    }
}
