package com.test.gcp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.test.gcp.payload.EmployeeDTO;
import com.test.gcp.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeDTO employeeDTO;
    private EmployeeDTO employeeDTO2;

    @BeforeEach
    void setUp() throws Exception {
        employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeId("1");
        employeeDTO.setName("Avirup");
        employeeDTO.setAddress("Kolkata");
        employeeDTO.setPhoneNumber("9807896543");
        employeeDTO.setDepartmentId("1");

        employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setEmployeeId("2");
        employeeDTO2.setName("Sumit");
        employeeDTO2.setAddress("Out Of Kolkata");
        employeeDTO2.setPhoneNumber("8907896543");
        employeeDTO2.setDepartmentId("2");
    }

    @Test
    void testCreateEmployee() {
        Mockito.when(employeeService.createEmployee(employeeDTO)).thenReturn(employeeDTO);
        ResponseEntity<EmployeeDTO> actualResponse = employeeController.createEmployee(employeeDTO);
        assertEquals("1", actualResponse.getBody().getEmployeeId());
    }

    @Test
    void testGetEmployees() {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        employeeDTOs.add(employeeDTO2);
        employeeDTOs.add(employeeDTO);

        Mockito.when(employeeService.getEmployees()).thenReturn(employeeDTOs);
        assertEquals(employeeDTOs, employeeController.getEmployees());
    }

    @Test
    void testGetEmployeesById() {
        Mockito.when(employeeService.getEmployeesById("1")).thenReturn(employeeDTO);
        ResponseEntity<EmployeeDTO> actualResponse = employeeController.getEmployeesById("1");
        assertEquals("1", actualResponse.getBody().getEmployeeId());
    }

    @Test
    void testGetEmployeesByDepartmentId() {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        employeeDTOs.add(employeeDTO2);
        employeeDTOs.add(employeeDTO);

        Mockito.when(employeeService.getEmployeesByDepartmentId("1")).thenReturn(employeeDTOs);
        assertEquals(employeeDTOs, employeeController.getEmployeesByDepartmentId("1"));
    }

    @Test
    void testUpdateEmployee() {
        Mockito.when(employeeService.updateEmployee("1", employeeDTO)).thenReturn(employeeDTO2);
        ResponseEntity<EmployeeDTO> actualResponse = employeeController.updateEmployee("1", employeeDTO);
        assertEquals("2", actualResponse.getBody().getEmployeeId());
    }

    @Test
    void testDeleteEmployee() {
        ResponseEntity<String> response = employeeController.deleteEmployee("1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Employee deleted successfully");
    }
}
