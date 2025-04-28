package com.team6.hrbank.service;

import com.team6.hrbank.dto.backup.BackupSearchCondition;
import com.team6.hrbank.dto.backup.CursorPageResponseBackupDto;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupHistory;
import com.team6.hrbank.entity.BackupStatus;
import com.team6.hrbank.entity.Employee;
import com.team6.hrbank.entity.FileMetadata;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.BackupMapper;
import com.team6.hrbank.repository.BackupHistoryRepository;
import com.team6.hrbank.repository.BackupHistoryRepositoryCustom;
import com.team6.hrbank.repository.EmployeeRepository;
import com.team6.hrbank.repository.FileMetadataRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BackupHistoryServiceImpl implements BackupHistoryService{
  private final BackupHistoryRepository backupHistoryRepository;
  private final FileMetadataRepository fileMetadataRepository;
  private final BackupMapper backupMapper;
  private final EmployeeRepository employeeRepository;
  private final ChangeLogService changeLogService;
  private final BackupHistoryRepositoryCustom backupHistoryRepositoryCustom;

  @Value("test_backupDirs")
  private String uploadDir;
  @Value("test_backErrorDirs")
  private String errorDir;

  /**
  수동 백업 생성
   **/
  @Transactional
  @Override
  public BackupDto create(HttpServletRequest request) {
    // ip 주소 수정 필요, 제대로 반영 안 되는 듯
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty()) {
      ip = request.getRemoteAddr();
    }
    return createBackup(ip);
  }

  /**
   * 자동 백업 1시간 주기
   */
  @Transactional
  @Override
  public BackupDto createSystem() {
    return createBackup("system");
  }

  /**
   * 1시간 주기 배치 스케줄러
   */
  @Transactional
  @Override
  public BackupDto createScheduledBackup() {
    // 가장 최근 완료된 백업 이력 가져오기
    BackupHistory latestBackup = backupHistoryRepository.findTopByStatusOrderByStartedAtDesc(BackupStatus.COMPLETED)
        .orElse(null);

    if (latestBackup != null) {
      Instant lastBackupTime = latestBackup.getStartedAt();

      // 최근 백업 이후 발생한 직원 변경 이력(변동 로그) 개수 조회
      Long changeLogCount = changeLogService.getCount(lastBackupTime, Instant.now());

      if (changeLogCount == 0) {
        // 변동이 없으면 SKIPPED 처리
        BackupDto skippedDto = new BackupDto(
            null, "system", Instant.now(), Instant.now(), BackupStatus.SKIPPED, null
        );
        BackupHistory skippedEntity = backupHistoryRepository.save(backupMapper.toEntity(skippedDto, null));
        return backupMapper.toDto(skippedEntity);
      }
    }
    // 변동이 있으면 정상적으로 백업 진행
    return createSystem();
  }

  @Transactional
  @Override
  public BackupDto findLatest(BackupStatus status) {
    BackupHistory backupHistory = backupHistoryRepository
        .findTopByStatusOrderByStartedAtDesc(status)
        .orElseThrow(() -> new RestException(ErrorCode.LATEST_BACKUP_NOT_FOUND));

    return backupMapper.toDto(backupHistory);
  }

  @Override
  public CursorPageResponseBackupDto searchBackupList(
      String worker,
      String status,
      String startedAtFrom,
      String startedAtTo,
      Long idAfter,
      String cursor,
      Integer size,
      String sortField,
      String sortDirection
  ) {
    BackupSearchCondition condition = new BackupSearchCondition(
        worker, status, startedAtFrom, startedAtTo, sortField, sortDirection, cursor, idAfter, size
    );

    List<BackupDto> results = backupHistoryRepositoryCustom.searchBackupHistoryWithCursor(condition);
    Long totalCount = backupHistoryRepositoryCustom.countBySearchCondition(condition);

    boolean hasNext = results.size() > size;
    if (hasNext) {
      results = results.subList(0, size);
    }

    BackupDto lastItem = results.isEmpty() ? null : results.get(results.size() - 1);

    return new CursorPageResponseBackupDto(
        new ArrayList<>(results),
        lastItem != null ? getCursorValue(lastItem, sortField) : null,
        lastItem != null ? lastItem.id() : null,
        size,
        totalCount,
        hasNext
    );
  }

  private String getCursorValue(BackupDto dto, String sortField) {
    if ("startedAt".equalsIgnoreCase(sortField) && dto.startedAt() != null) {
      return dto.startedAt().toString();
    } else if ("endedAt".equalsIgnoreCase(sortField) && dto.endedAt() != null) {
      return dto.endedAt().toString();
    }
    return null;
  }


  // 백업 생성
  private BackupDto createBackup(String worker) {
    Instant now = Instant.now();

    BackupDto dto = new BackupDto(null, worker, now, null, BackupStatus.IN_PROGRESS, null);
    BackupHistory savedEntity = backupHistoryRepository.save(backupMapper.toEntity(dto, null));

    try {
      // 성공시 파일 작성
      FileMetadata savedMetadata = backupEmployeesToCsv(savedEntity, worker);
      savedEntity.updateCompleted(savedMetadata);
      return backupMapper.toDto(savedEntity);
    } catch (Exception e) {
      // 실패시
      handleBackupFailure(savedEntity, e);
      return backupMapper.toDto(savedEntity);
    }
  }

  // 성공시 백업 로직
  private FileMetadata backupEmployeesToCsv(BackupHistory backupHistory, String worker) throws IOException {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    int randomSixDigit;
    String fileName;
    Path filePath;
    File file;

    do {
      randomSixDigit = (int) (Math.random() * 900000) + 100000;
      fileName = String.format("employee_backup_%d_%s_%d.csv", backupHistory.getId(), timestamp, randomSixDigit);
      filePath = Paths.get(uploadDir, fileName);
      file = filePath.toFile();
    } while (file.exists());

    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write("id,employeeNumber,employeeName,email,departmentName,position,hireDate,employeeState");
      writer.newLine();

      long lastId = 0L;
      boolean hasMore = true;
      while (hasMore) {
        List<Employee> batch = employeeRepository.findTop20ByIdGreaterThanOrderByIdAsc(lastId);
        if (batch.isEmpty()) {
          hasMore = false;
        } else {
          for (Employee emp : batch) {
            String line = String.join(",",
                String.valueOf(emp.getId()),
                emp.getEmployeeNumber(),
                emp.getEmployeeName(),
                emp.getEmail(),
                emp.getDepartment() != null ? emp.getDepartment().getDepartmentName() : "",
                emp.getEmployeePosition() != null ? emp.getEmployeePosition().name() : "",
                emp.getHireDate() != null ? emp.getHireDate().toString() : "",
                emp.getEmployeeState() != null ? emp.getEmployeeState().name() : ""
            );
            writer.write(line);
            writer.newLine();
            lastId = emp.getId();
          }
        }
      }
      writer.flush();
    }

    FileMetadata metadata = FileMetadata.builder()
        .fileName(fileName)
        .contentType("text/csv")
        .fileSize((int) file.length())
        .build();
    return fileMetadataRepository.save(metadata);
  }

  // 실패 시, .log 파일 남기기
  private void handleBackupFailure(BackupHistory savedEntity, Exception e) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd_HHmmss"));
    int randomSixDigit = (int) (Math.random() * 900000) + 100000;
    String errorLogFileName = String.format("employee_backup_error_%d_%s_%d.log",
        savedEntity.getId(), timestamp, randomSixDigit);
    Path errorLogPath = Paths.get(errorDir, errorLogFileName);
    File errorFile = errorLogPath.toFile();

    try {
      if (!errorFile.getParentFile().exists()) {
        errorFile.getParentFile().mkdirs();
      }

      try (BufferedWriter errorWriter = new BufferedWriter(new FileWriter(errorFile))) {
        errorWriter.write("백업 실패 에러 메시지:\n");
        errorWriter.write(e.getMessage());
      }

      FileMetadata errorMetadata = FileMetadata.builder()
          .fileName(errorLogFileName)
          .contentType("text/plain")
          .fileSize((int) errorFile.length())
          .build();
      FileMetadata savedErrorMetadata = fileMetadataRepository.save(errorMetadata);

      savedEntity.updateFailed(savedErrorMetadata);
      savedEntity.setEndedAt(Instant.now());
    } catch (IOException ioException) {
      throw new RuntimeException("에러 로그 파일 작성 실패", ioException);
    }
  }
}
