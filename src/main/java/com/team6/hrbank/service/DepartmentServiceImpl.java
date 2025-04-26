package com.team6.hrbank.service;

import com.team6.hrbank.dto.department.CursorPageResponseDepartmentDto;
import com.team6.hrbank.dto.department.DepartmentCreateRequest;
import com.team6.hrbank.dto.department.DepartmentDto;
import com.team6.hrbank.dto.department.DepartmentSearchCondition;
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
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;

  @Override
  @Transactional
  public DepartmentDto create(DepartmentCreateRequest request) {
    if (departmentRepository.existsByDepartmentName(request.name())) {
      throw new RestException(ErrorCode.DUPLICATE_DEPARTMENT);
    }

    Department department = departmentMapper.toEntity(request);
    return departmentMapper.toDto(departmentRepository.save(department), departmentRepository.countEmployeesByDepartmentId(department.getId()));
  }

  @Override
  @Transactional(readOnly = true)
  public DepartmentDto findById(Long id) {
    Department department = departmentRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));
    return departmentMapper.toDto(department, departmentRepository.countEmployeesByDepartmentId(department.getId()));
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseDepartmentDto getDepartments(DepartmentSearchCondition condition) {
    List<Department> departments = departmentRepository.searchDepartmentsWithCursor(condition);
    List<DepartmentDto> content = departments.stream()
        .map(d -> departmentMapper.toDto(d, departmentRepository.countEmployeesByDepartmentId(d.getId())))
        .toList();

    Long nextIdAfter = content.isEmpty() ? null : content.get(content.size() - 1).id();
    String nextCursor = null;

    if (!content.isEmpty()) {
      Department lastDepartment = departments.get(departments.size() - 1);
      nextIdAfter = lastDepartment.getId();
      if ("name".equals(condition.sortField())) nextCursor = lastDepartment.getDepartmentName();
      else if ("establishedDate".equals(condition.sortField())) nextCursor = String.valueOf(
          lastDepartment.getDepartmentEstablishedDate());
    }

    return new CursorPageResponseDepartmentDto(
        content,
        nextCursor,
        nextIdAfter,
        condition.size(),
        departmentRepository.count(),
        departments.size() > condition.size()
    );
  }


  @Override
  @Transactional
  public DepartmentDto update(Long id, DepartmentUpdateRequest request) {
    Department updateDepartment = departmentRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));
    if (!updateDepartment.getDepartmentName().equals(request.name()) && departmentRepository.existsByDepartmentName(request.name())) {
      throw new RestException(ErrorCode.DUPLICATE_DEPARTMENT);
    }

    updateDepartment.update(request);
    return departmentMapper.toDto(departmentRepository.save(updateDepartment), departmentRepository.countEmployeesByDepartmentId(updateDepartment.getId()));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Department department = departmentRepository.findById(id).orElseThrow(() -> new RestException(ErrorCode.DEPARTMENT_NOT_FOUND));
    if (departmentRepository.countEmployeesByDepartmentId(department.getId()) > 0) {
      throw new RestException(ErrorCode.CANNOT_DELETE_DEPARTMENT);
    }

    departmentRepository.delete(department);
  }

}
