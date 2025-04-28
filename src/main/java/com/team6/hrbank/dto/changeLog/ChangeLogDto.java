package com.team6.hrbank.dto.changeLog;

import com.team6.hrbank.entity.ChangeType;
import java.time.Instant;

public record ChangeLogDto(
    Long id,
    ChangeType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant at
) {

}
