package com.team6.hrbank.dto.data;

import com.team6.hrbank.entity.BackupStatus;
import java.time.Instant;

public record BackupDto(
    Long id,
    String worker,
    Instant startedAt,
    Instant endedAt,
    BackupStatus status,
    Long fileId
) {
}
