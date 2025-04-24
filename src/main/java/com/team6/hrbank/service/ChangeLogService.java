package com.team6.hrbank.service;

import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.dto.changeLog.DiffDto;
import java.util.List;

public interface ChangeLogService {

  CursorPageResponseChangeLogDto getChangeLogs(ChangeLogSearchCondition condition);
  List<DiffDto> getChangeDetail(Long changeLogId);

}
