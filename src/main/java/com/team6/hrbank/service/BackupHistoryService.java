package com.team6.hrbank.service;

import com.team6.hrbank.dto.backup.CursorPageResponseBackupDto;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupStatus;
import jakarta.servlet.http.HttpServletRequest;

public interface BackupHistoryService {
  BackupDto create(HttpServletRequest request);
  BackupDto createSystem();
  BackupDto createScheduledBackup();
  BackupDto findLatest(BackupStatus status);
  CursorPageResponseBackupDto searchBackupList(
      String worker,
      String status,
      String startedAtFrom,
      String startedAtTo,
      Long idAfter,
      String cursor,
      Integer size,
      String sortField,
      String sortDirection
  );

}
