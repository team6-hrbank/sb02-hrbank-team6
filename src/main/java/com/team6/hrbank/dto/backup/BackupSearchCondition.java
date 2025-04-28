package com.team6.hrbank.dto.backup;

public record BackupSearchCondition(
    String worker,
    String status,
    String startedAtFrom,
    String startedAtTo,
    String sortField,
    String sortDirection,
    String cursor,
    Long idAfter,
    int size
) {
}
