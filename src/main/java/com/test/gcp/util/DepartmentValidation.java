package com.test.gcp.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.test.gcp.exception.ResourceFoundException;
import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.DepartmentDTO;
import com.test.gcp.service.DepartmentService;

@Component
public class DepartmentValidation {

    public void validateAddDepartment(final DepartmentDTO departmentDTO) {

        Optional<DepartmentDTO> optionalDepartmentDTO = DepartmentService.DEPARTMENTS.stream().filter(e -> e.getDepartmentName().equalsIgnoreCase(departmentDTO.getDepartmentName())).findFirst();
        if (optionalDepartmentDTO.isPresent()) {
            throw new ResourceFoundException("Name", departmentDTO.getDepartmentName());
        }

    }

    public DepartmentDTO validateUpdateDepartment(final DepartmentDTO departmentDTO, final String departmentId) {

        Optional<DepartmentDTO> presentDepartmentDTO = DepartmentService.DEPARTMENTS.stream().filter(e -> e.getDepartmentId().equals(departmentId)).findFirst();
        if (!presentDepartmentDTO.isPresent()) {
            throw new ResourceNotFoundException("departmentId", departmentId);
        } else {
            Optional<DepartmentDTO> optionalDepartmentDTO = DepartmentService.DEPARTMENTS.stream().filter(e -> e.getDepartmentName().equalsIgnoreCase(departmentDTO.getDepartmentName()) && !e.getDepartmentId().equals(departmentId)).findFirst();
            if (optionalDepartmentDTO.isPresent()) {
                throw new ResourceFoundException("Name", departmentDTO.getDepartmentName());
            }
        }
        return presentDepartmentDTO.get();
    }
}
