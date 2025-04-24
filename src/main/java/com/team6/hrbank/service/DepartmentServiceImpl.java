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
import com.team6.hrbank.specification.DepartmentSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    Sort sort = Sort.by(
        Sort.Direction.fromString(condition.sortDirection()),
        condition.sortField().equals("name") ? "departmentName" : "departmentEstablishedDate"
    ).and(Sort.by("id"));

    Pageable pageable = PageRequest.of(0, condition.size(), sort);

    Specification<Department> spec = DepartmentSpecification.withConditions(condition);

    Page<Department> page = departmentRepository.findAll(spec, pageable);
    List<Department> departments = page.getContent();

    List<DepartmentDto> content = departments.stream()
        .map(d -> departmentMapper.toDto(d, departmentRepository.countEmployeesByDepartmentId(d.getId())))
        .toList();

    DepartmentDto last = content.isEmpty() ? null : content.get(content.size() - 1);

    String nextCursor = null;
    Long nextIdAfter = null;

    if (last != null) {
      nextCursor = condition.sortField().equals("name") ? last.name() : last.establishedDate().toString();
      nextIdAfter = last.id();
    }

    return new CursorPageResponseDepartmentDto(
        content,
        nextCursor,
        nextIdAfter,
        condition.size(),
        page.getTotalElements(),
        page.hasNext()
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
