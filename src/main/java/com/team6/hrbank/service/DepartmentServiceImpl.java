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
    return departmentMapper.toDto(departmentRepository.save(department));
  }

  @Override
  public DepartmentDto findById(Long id) {
    Department department = departmentRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.NOT_FOUND));
    return departmentMapper.toDto(department);
  }

  @Override
  public List<DepartmentDto> findAll() {
    return List.of();
  }

  @Override
  public DepartmentDto update(DepartmentUpdateRequest request) {
    return null;
  }

  @Override
  public void deleteById(Long id) {

  }
}
