package com.team6.hrbank.dto.department;

public record DepartmentSearchCondition(
    String nameOrDescription,
    String sortField,
    String sortDirection,
    String cursor,
    Long idAfter,
    int size
) {}
