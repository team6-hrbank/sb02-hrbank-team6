package com.team6.hrbank.scheduler;

import com.team6.hrbank.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BackupHistoryScheduler {

  private final BackupHistoryService backupHistoryService;

  @Scheduled(cron = "0 0 * * * *") // 매 시간 0분마다
//  @Scheduled(cron = "0 28 * * * *")
  public void scheduleBackup() {
    try {
      log.info("자동 백업 시작");
      backupHistoryService.createScheduledBackup();
      log.info("자동 백업 완료");
    } catch (Exception e) {
      log.error("자동 백업 실패", e);
    }
  }
}

