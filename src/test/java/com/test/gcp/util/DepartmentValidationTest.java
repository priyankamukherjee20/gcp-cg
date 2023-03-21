package com.test.gcp.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.test.gcp.exception.ResourceFoundException;
import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.DepartmentDTO;
import com.test.gcp.service.DepartmentService;

@ExtendWith(MockitoExtension.class) @TestMethodOrder(OrderAnnotation.class)
class DepartmentValidationTest {

    @InjectMocks
    private DepartmentValidation departmentValidation;

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
        DepartmentService.DEPARTMENTS.clear();
    }

    @Test @Order(1)
    void testValidateAddDepartment() {
        departmentValidation.validateAddDepartment(departmentDTO);
    }

    @Test @Order(2)
    void testValidateAddDepartment_ResourceFoundException() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        Throwable throwable = assertThrows(ResourceFoundException.class, () -> departmentValidation.validateAddDepartment(departmentDTO));
        assertEquals("Name already found with : 'FS'", throwable.getMessage());
    }

    @Test @Order(3)
    void testValidateUpdateDepartment() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        departmentValidation.validateUpdateDepartment(departmentDTO, "1");
    }

    @Test @Order(4)
    void testValidateUpdateDepartment_ResourceNotFoundException() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> departmentValidation.validateUpdateDepartment(departmentDTO, "3"));
        assertEquals("departmentId not found with : '3'", throwable.getMessage());
    }

    @Test @Order(5)
    void testValidateUpdateDepartment_ResourceFoundException() {
        DepartmentService.DEPARTMENTS.addAll(departmentDTOs);
        Throwable throwable = assertThrows(ResourceFoundException.class, () -> departmentValidation.validateUpdateDepartment(departmentDTO, "2"));
        assertEquals("Name already found with : 'FS'", throwable.getMessage());
    }
}
