package com.test.gcp.service;

import java.util.LinkedList;
import java.util.List;

import com.test.gcp.payload.DepartmentDTO;

public interface DepartmentService {

     List<DepartmentDTO> DEPARTMENTS = new LinkedList<>();

     DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

     List<DepartmentDTO> getDepartments();

     DepartmentDTO getDepartmentsById(String departmentId);

     DepartmentDTO updateDepartment(String departmentId, DepartmentDTO departmentDTO);

     void deleteDepartment(String departmentId);
}
