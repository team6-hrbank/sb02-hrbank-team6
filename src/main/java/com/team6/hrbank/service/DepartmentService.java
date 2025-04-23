package com.team6.hrbank.service;

import com.team6.hrbank.dto.department.DepartmentDto;
import com.team6.hrbank.dto.department.DepartmentCreateRequest;
import com.team6.hrbank.dto.department.DepartmentUpdateRequest;
import java.util.List;

public interface DepartmentService {
  DepartmentDto create(DepartmentCreateRequest request);
  DepartmentDto findById(Long id);
  List<DepartmentDto> findAll();
  DepartmentDto update(Long id, DepartmentUpdateRequest request);
  void deleteById(Long id);
}
