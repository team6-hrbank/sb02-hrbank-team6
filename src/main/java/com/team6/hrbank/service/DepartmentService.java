package com.team6.hrbank.service;

import com.team6.hrbank.dto.department.CursorPageResponseDepartmentDto;
import com.team6.hrbank.dto.department.DepartmentCreateRequest;
import com.team6.hrbank.dto.department.DepartmentDto;
import com.team6.hrbank.dto.department.DepartmentSearchCondition;
import com.team6.hrbank.dto.department.DepartmentUpdateRequest;

public interface DepartmentService {
  DepartmentDto create(DepartmentCreateRequest request);
  DepartmentDto findById(Long id);
  DepartmentDto update(Long id, DepartmentUpdateRequest request);
  void deleteById(Long id);
  CursorPageResponseDepartmentDto getDepartments(DepartmentSearchCondition condition);
}
