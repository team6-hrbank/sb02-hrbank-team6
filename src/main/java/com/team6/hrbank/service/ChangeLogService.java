package com.team6.hrbank.service;

import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.dto.changeLog.DiffDto;
import com.team6.hrbank.entity.ChangeLog;
import com.team6.hrbank.entity.Employee;
import java.time.Instant;
import java.util.List;

public interface ChangeLogService {

  ChangeLog create(Employee employeeBefore, Employee employeeAfter, String memo, String ipAddress);
  CursorPageResponseChangeLogDto getChangeLogs(ChangeLogSearchCondition condition);
  List<DiffDto> getChangeDetail(Long changeLogId);
  long getCount(Instant fromDate, Instant toDate);

}
