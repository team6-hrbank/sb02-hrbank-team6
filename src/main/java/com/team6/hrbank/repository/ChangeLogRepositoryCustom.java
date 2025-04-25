package com.team6.hrbank.repository;

import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.entity.ChangeLog;
import java.util.List;

public interface ChangeLogRepositoryCustom {
  List<ChangeLog> findAllByFilter(ChangeLogSearchCondition conditions, int limit);
}
