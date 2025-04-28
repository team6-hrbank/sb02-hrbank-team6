package com.team6.hrbank.mapper;

import com.team6.hrbank.dto.employee.EmployeeCreateRequest;
import com.team6.hrbank.dto.employee.EmployeeDto;
import com.team6.hrbank.entity.Department;
import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.entity.EmployeePosition;
import com.team6.hrbank.entity.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeNumber", expression = "java(employeeNumber)")
    @Mapping(target = "employeeName", source = "request.name")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "employeePosition", source = "position")
    @Mapping(target = "hireDate", source = "request.hireDate")
    @Mapping(target = "employeeState", ignore = true)
    @Mapping(target = "profileImage", source = "profileImage")
    Employee toEntity(EmployeeCreateRequest request, String employeeNumber, Department department, EmployeePosition position, FileMetadata profileImage);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "employeeName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.departmentName")
    @Mapping(target = "position", source = "employeePosition",qualifiedByName = "enumToString")
    @Mapping(target = "hireDate", source = "hireDate")
    @Mapping(target = "status", source = "employeeState",qualifiedByName = "enumToString")
    @Mapping(target = "profileImageId", source = "profileImage.id")
    EmployeeDto toDto(Employee employee);

    @Named("enumToString")
    static String enumToString(Enum<?> e) {
        return e != null ? e.name() : null;
    }
}
