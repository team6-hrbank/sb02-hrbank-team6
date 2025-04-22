package com.team6.hrbank.dto.employee;

import com.team6.hrbank.entity.EmployeePosition;
import com.team6.hrbank.entity.EmployeeState;

import java.time.LocalDate;

public record EmployeeDto(
        Long id,
        String name,
        String email,
        String employeeNumber,
        Long departmentId,
        String departmentName,
        EmployeePosition position,
        LocalDate hireDate,
        EmployeeState status,
        Long profileImageId
) {

}
