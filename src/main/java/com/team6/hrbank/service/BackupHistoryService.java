package com.team6.hrbank.service;

import com.team6.hrbank.dto.backup.CursorPageResponseBackupDto;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupStatus;

public interface BackupHistoryService {
  /* create, update, findbyId?recent, findAll */

  BackupDto create();
  BackupDto update();
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
