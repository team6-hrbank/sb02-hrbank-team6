package com.team6.hrbank.dto.backup;

import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupHistory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public record CursorPageResponseBackupDto(
    ArrayList<BackupDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
