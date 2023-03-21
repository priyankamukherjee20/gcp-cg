package com.test.gcp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
import com.test.gcp.service.DepartmentService;
import com.test.gcp.util.DepartmentValidation;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @InjectMocks
    private DepartmentServiceImpl departmentServiceImpl;

    @Mock
    private DepartmentValidation departmentValidation;

    private DepartmentDTO departmentDTO;
    private DepartmentDTO departmentDTO2;
    private List<DepartmentDTO> departmentDTOs;

    @BeforeEach
    void setUp() throws Exception {
        JsonReader<DepartmentDTO> mapper = new JsonReader<DepartmentDTO>(DepartmentDTO.class);
        departmentDTO = (DepartmentDTO) mapper.loadTestJson("test-resources/department/department.json");
        departmentDTO2 = (DepartmentDTO) mapper.loadTestJson("test-resources/department/department2.json");
      //  departmentDTOs = mapper.loadTestJsonArray("test-resources/department/departments.json");
        departmentDTOs = new ArrayList<>();
        departmentDTOs.add(departmentDTO);
        departmentDTOs.add(departmentDTO2);
        System.out.println("hello world");
    }

    @Test
    void testCreateDepartment() {
        Mockito.doNothing().when(departmentValidation).validateAddDepartment(departmentDTO);
        DepartmentDTO actualResponse = departmentServiceImpl.createDepartment(departmentDTO);
        assertEquals(String.valueOf(DepartmentService.DEPARTMENTS.size()), actualResponse.getDepartmentId());
    }

    @Test
    void testGetDepartments() {
        System.out.println(departmentDTOs);
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        assertEquals(DepartmentService.DEPARTMENTS.size(), departmentServiceImpl.getDepartments().size());

    }

    @Test
    void testGetDepartmentsById() {
       // System.out.println(departmentDTOs);
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);

        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        System.out.println(DepartmentService.DEPARTMENTS);
        DepartmentDTO actualResponse = departmentServiceImpl.getDepartmentsById("1");
        System.out.println(actualResponse);
       // assertEquals("1", actualResponse.getDepartmentId());
    }

    @Test
    void testGetDepartmentsById_ResourceFoundException() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> departmentServiceImpl.getDepartmentsById("20"));
        assertEquals("departmentId not found with : '20'", throwable.getMessage());
    }

    @Test
    void testUpdateDepartment() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        DepartmentDTO actualResponse = departmentServiceImpl.updateDepartment("1", departmentDTO);
        assertEquals("1", actualResponse.getDepartmentId());
    }

    @Test
    void testDeleteDepartment() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        departmentServiceImpl.deleteDepartment("2");
        // Mockito.verify(DepartmentService.DEPARTMENTS.remove(departmentDTO),
        // Mockito.times(1));
    }

    @Test
    void testDeleteDepartment_ResourceFoundException() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> departmentServiceImpl.deleteDepartment("20"));
        assertEquals("departmentId not found with : '20'", throwable.getMessage());
    }
}
