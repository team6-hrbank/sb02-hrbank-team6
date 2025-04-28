package com.team6.hrbank.repository;

import com.team6.hrbank.dto.backup.BackupSearchCondition;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.dto.department.DepartmentSearchCondition;
import com.team6.hrbank.entity.Department;
import java.util.List;

public interface BackupHistoryRepositoryCustom {
  List<BackupDto> searchBackupHistoryWithCursor(BackupSearchCondition condition);
  Long countBySearchCondition(BackupSearchCondition condition);
}
