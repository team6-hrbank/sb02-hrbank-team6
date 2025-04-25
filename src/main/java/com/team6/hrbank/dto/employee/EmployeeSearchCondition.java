package com.team6.hrbank.dto.employee;

import java.time.LocalDate;

public record EmployeeSearchCondition(
        String nameOrEmail,
        String departmentName,
        String position,
        String employeeNumber,
        LocalDate hireDateFrom,
        LocalDate hireDateTo,
        String status,

        Long idAfter,
        String cursor,
        Integer size,
        String sortField,
        String sortDirection
) {
}

