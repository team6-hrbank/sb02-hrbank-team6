package com.team6.hrbank.service;

import com.team6.hrbank.dto.department.DepartmentCreateRequest;
import com.team6.hrbank.dto.department.DepartmentDto;
import com.team6.hrbank.dto.department.DepartmentUpdateRequest;
import com.team6.hrbank.entity.Department;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.DepartmentMapper;
import com.team6.hrbank.repository.DepartmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;

  @Override
  public DepartmentDto create(DepartmentCreateRequest request) {
    if (departmentRepository.existsByDepartmentName(request.name())) {
      throw new RestException(ErrorCode.DUPLICATE_DEPARTMENT);
    }

    Department department = departmentMapper.toEntity(request);
    return departmentMapper.toDto(departmentRepository.save(department), departmentRepository.countEmployees(department.getId()));
  }

  @Override
  public DepartmentDto findById(Long id) {
    Department department = departmentRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));
    return departmentMapper.toDto(department, departmentRepository.countEmployees(department.getId()));
  }

  @Override
  public List<DepartmentDto> findAll() {
    return List.of();
  }

  @Override
  public DepartmentDto update(Long id, DepartmentUpdateRequest request) {
    Department updateDepartment = departmentRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));
    if (!updateDepartment.getDepartmentName().equals(request.name()) && departmentRepository.existsByDepartmentName(request.name())) {
      throw new RestException(ErrorCode.DUPLICATE_DEPARTMENT);
    }

    updateDepartment.update(request);
    return departmentMapper.toDto(departmentRepository.save(updateDepartment), departmentRepository.countEmployees(updateDepartment.getId()));
  }

  @Override
  public void deleteById(Long id) {
    Department department = departmentRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));
    if (departmentRepository.countEmployees(department.getId()) > 0) {
      throw new RestException(ErrorCode.CANNOT_DELETE_DEPARTMENT);
    }

    departmentRepository.delete(department);
  }
}
