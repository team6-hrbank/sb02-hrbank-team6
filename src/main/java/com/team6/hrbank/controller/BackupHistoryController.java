package com.team6.hrbank.controller;

import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupStatus;
import com.team6.hrbank.service.BackupHistoryService;
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

  @PostMapping
  public ResponseEntity<BackupDto> createBackup() {
    return ResponseEntity.ok(backupHistoryService.create());
  }

  @GetMapping("/latest")
  public ResponseEntity<BackupDto> findLatest(
      @RequestParam(defaultValue = "COMPLETED") BackupStatus status) {
    System.out.println("최근 이력 조회 요청 들어옴.");
    return ResponseEntity.ok(backupHistoryService.findLatest(status));
  }
}
