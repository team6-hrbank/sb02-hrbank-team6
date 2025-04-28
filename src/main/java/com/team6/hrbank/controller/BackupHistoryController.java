package com.team6.hrbank.controller;

import static com.team6.hrbank.entity.BackupStatus.IN_PROGRESS;

import com.team6.hrbank.dto.backup.BackupSearchCondition;
import com.team6.hrbank.dto.backup.CursorPageResponseBackupDto;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupStatus;
import com.team6.hrbank.service.BackupHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import com.team6.hrbank.swagger.BackupHistoryApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupHistoryController implements BackupHistoryApi {
  private final BackupHistoryService backupHistoryService;

  @GetMapping
  public ResponseEntity<CursorPageResponseBackupDto> searchBackupList(
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String startedAtFrom,
      @RequestParam(required = false) String startedAtTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "startedAt") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      @RequestParam(defaultValue = "15") Integer size
  ) {
    var result = backupHistoryService.searchBackupList(
        worker, status, startedAtFrom, startedAtTo, idAfter, cursor, size, sortField, sortDirection
    );
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<BackupDto> createBackup(HttpServletRequest request) {
    return ResponseEntity.ok(backupHistoryService.create(request));
  }

  @GetMapping("/latest")
  public ResponseEntity<BackupDto> findLatest(
      @RequestParam(defaultValue = "COMPLETED") BackupStatus status) {
    return ResponseEntity.ok(backupHistoryService.findLatest(status));
  }

}
