package com.team6.hrbank.mapper;

import com.team6.hrbank.dto.department.DepartmentCreateRequest;
import com.team6.hrbank.dto.department.DepartmentDto;
import com.team6.hrbank.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
  @Mapping(source = "name", target = "departmentName")
  @Mapping(source = "description", target = "departmentDescription")
  @Mapping(source = "establishedDate", target = "departmentEstablishedDate")
  Department toEntity(DepartmentCreateRequest request);

  @Mapping(source = "department.departmentName", target = "name")
  @Mapping(source = "department.departmentDescription", target = "description")
  @Mapping(source = "department.departmentEstablishedDate", target = "establishedDate")
  @Mapping(source = "employeeCount", target = "employeeCount")
  DepartmentDto toDto(Department department, Long employeeCount);
}
