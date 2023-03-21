package com.test.gcp.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.gcp.exception.ResourceNotFoundException;
import com.test.gcp.payload.DepartmentDTO;
import com.test.gcp.service.DepartmentService;
import com.test.gcp.util.DepartmentValidation;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LogManager.getLogger(DepartmentServiceImpl.class);

    @Autowired
    private DepartmentValidation departmentValidation;

    @Override
    public DepartmentDTO createDepartment(final DepartmentDTO departmentDTO) {

        logger.log(Level.INFO, () -> "createDepartment method starts ===>>> " + departmentDTO);

        departmentValidation.validateAddDepartment(departmentDTO);
        String departmentId = String.valueOf(DEPARTMENTS.size() + 1);
        departmentDTO.setDepartmentId(departmentId);
        DEPARTMENTS.add(departmentDTO);

        logger.log(Level.INFO, () -> "createDepartment method ends ===>>> " + departmentDTO);
        return departmentDTO;
    }

    @Override
    public List<DepartmentDTO> getDepartments() {
        logger.log(Level.INFO, () -> "getDepartments method");
        return DEPARTMENTS;
    }

    @Override
    public DepartmentDTO getDepartmentsById(final String departmentId) {

        logger.log(Level.INFO, () -> "getDepartmentsById method starts ===>>> " + departmentId);
        DepartmentDTO presentDepartmentDTO = null;
        Optional<DepartmentDTO> departmentDTO = DEPARTMENTS.stream()
                .filter(e -> e.getDepartmentId().equals(departmentId)).findFirst();
        System.out.println("departmentDTO: " + departmentDTO);
        if (departmentDTO.isPresent()) {
            presentDepartmentDTO = departmentDTO.get();
        } else {
            throw new ResourceNotFoundException("departmentId", departmentId);
        }
        logger.log(Level.INFO, "getDepartmentsById method ends ===>>> " + presentDepartmentDTO);
        return presentDepartmentDTO;
    }

    @Override
    public DepartmentDTO updateDepartment(final String departmentId,
                                          final DepartmentDTO departmentDTO) {

        logger.log(Level.INFO, () -> "updateDepartment method starts ===>>> " + departmentId);

        DepartmentDTO departmentDTO2 = departmentValidation.validateUpdateDepartment(departmentDTO,
                departmentId);
        DEPARTMENTS.remove(departmentDTO2);
        departmentDTO.setDepartmentId(departmentId);
        DEPARTMENTS.add(departmentDTO);
        logger.log(Level.INFO, () -> "updateDepartment method ends ===>>> " + DEPARTMENTS);
        return departmentDTO;
    }

    @Override
    public void deleteDepartment(final String departmentId) {

        logger.log(Level.INFO, () -> "deleteDepartment method starts ===>>> " + departmentId);

        Optional<DepartmentDTO> departmentDTO = DEPARTMENTS.stream()
                .filter(e -> e.getDepartmentId().equals(departmentId)).findFirst();
        if (departmentDTO.isPresent()) {
            DEPARTMENTS.remove(departmentDTO.get());
        } else {
            throw new ResourceNotFoundException("departmentId", departmentId);
        }
        logger.log(Level.INFO, () -> "deleteDepartment method ends ===>>> " + DEPARTMENTS);
    }
}
