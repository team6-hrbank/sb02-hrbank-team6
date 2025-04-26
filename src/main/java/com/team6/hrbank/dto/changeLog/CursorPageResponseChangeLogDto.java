package com.team6.hrbank.dto.changeLog;

import java.util.List;

public record CursorPageResponseChangeLogDto(
    List<ChangeLogDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}
