package com.team6.hrbank.dto.employee;

import java.util.List;

public record CursorPageResponseEmployeeDto(
        List<EmployeeDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        long totalElements,
        boolean hasNext
) {
}
