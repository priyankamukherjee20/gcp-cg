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

import com.test.gcp.payload.DepartmentDTO;
import com.test.gcp.service.DepartmentService;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private DepartmentDTO departmentDTO;
    private DepartmentDTO departmentDTO2;

    @BeforeEach
    void setUp() throws Exception {
        departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId("1");
        departmentDTO.setDepartmentName("FS");

        departmentDTO2 = new DepartmentDTO();
        departmentDTO2.setDepartmentId("2");
        departmentDTO2.setDepartmentName("NON-FS");
    }

    @Test
    void testCreateDepartment() {
        Mockito.when(departmentService.createDepartment(departmentDTO)).thenReturn(departmentDTO);
        ResponseEntity<DepartmentDTO> actualResponse = departmentController.createDepartment(departmentDTO);
        assertEquals("1", actualResponse.getBody().getDepartmentId());
    }

    @Test
    void testGetDepartments() {
        List<DepartmentDTO> departmentDTOs = new ArrayList<>();
        departmentDTOs.add(departmentDTO2);
        departmentDTOs.add(departmentDTO);

        Mockito.when(departmentService.getDepartments()).thenReturn(departmentDTOs);
        assertEquals(departmentDTOs, departmentController.getDepartments());
    }

    @Test
    void testGetDepartmentsById() {
        Mockito.when(departmentService.getDepartmentsById("1")).thenReturn(departmentDTO);
        ResponseEntity<DepartmentDTO> actualResponse = departmentController.getDepartmentsById("1");
        assertEquals("1", actualResponse.getBody().getDepartmentId());
    }

    @Test
    void testUpdateDepartment() {
        Mockito.when(departmentService.updateDepartment("1", departmentDTO)).thenReturn(departmentDTO2);
        ResponseEntity<DepartmentDTO> actualResponse = departmentController.updateDepartment("1", departmentDTO);
        assertEquals("2", actualResponse.getBody().getDepartmentId());
    }

    @Test
    void testDeleteDepartment() {
        ResponseEntity<String> response = departmentController.deleteDepartment("1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Department deleted successfully");
    }
}
