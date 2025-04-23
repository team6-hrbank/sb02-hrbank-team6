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

  @Mapping(source = "departmentName", target = "name")
  @Mapping(source = "departmentDescription", target = "description")
  @Mapping(source = "departmentEstablishedDate", target = "establishedDate")
  @Mapping(target = "employeeCount", expression = "java((long) department.getEmployeeCount())")
  DepartmentDto toDto(Department department);
}
