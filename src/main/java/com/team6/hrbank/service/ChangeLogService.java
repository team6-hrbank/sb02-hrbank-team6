package com.team6.hrbank.service;

import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;

public interface ChangeLogService {

  CursorPageResponseChangeLogDto getChangeLogs(ChangeLogSearchCondition condition);

}
