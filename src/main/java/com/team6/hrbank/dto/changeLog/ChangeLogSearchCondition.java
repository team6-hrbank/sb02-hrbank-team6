package com.team6.hrbank.dto.changeLog;

import com.team6.hrbank.entity.ChangeType;
import java.time.Instant;

public record ChangeLogSearchCondition(
    String employeeNumber,
    ChangeType type,
    String memo,
    String ipAddress,
    Instant atFrom,
    Instant atTo,
    Long idAfter,
    String cursor,
    int size,
    String sortField,
    String sortDirection
) {

}
