package com.team6.hrbank.dto.department;

import java.util.List;

public record CursorPageResponseDepartmentDto(
    List<DepartmentDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
