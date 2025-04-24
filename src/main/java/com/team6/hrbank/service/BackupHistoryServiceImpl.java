package com.team6.hrbank.service;

import com.team6.hrbank.dto.backup.CursorPageResponseBackupDto;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupHistory;
import com.team6.hrbank.entity.BackupStatus;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.mapper.BackupMapper;
import com.team6.hrbank.repository.BackupHistoryRepository;
import com.team6.hrbank.repository.FileMetadataRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BackupHistoryServiceImpl implements BackupHistoryService{
  private final BackupHistoryRepository backupHistoryRepository;
  private final FileMetadataRepository fileMetadataRepository;
  private final BackupMapper backupMapper;

  @Transactional
  @Override
  public BackupDto create() {
    // 고려사항: 모든 직원 한번에 처리, OOM 이슈 -> 일정 수 20개씩 나눠서 조회 + 20개씩 나눠서 작성
    String worker = "127.0.0.1"; // 일단 하드 코딩, 추후 변경 예정
    Instant now = Instant.now();

    // Dto 생성
    BackupDto dto = new BackupDto(
        null,
        worker,
        now,
        null,
        BackupStatus.IN_PROGRESS,
        null
    );

    // 엔터티로 변환해서 DB에 저장
    BackupHistory toEntity = backupMapper.toEntity(dto, null);
    BackupHistory savedEntity = backupHistoryRepository.save(toEntity);

    // 최근 백업 파일 id, 시작 시간 조회
    // 전체 직원 조회 (나눠서)
    // 직원 데이터 csv파일에 작성 -> 파일명: employee_backup_{id}_{시작시간 년월일, 250421}_{6자리 난수}
    // 파일 지정한 경로에 복사
    // filemetatdatarepository.save()
    // 파일(id)을 backuphistory 테이블에 업데이트


    return backupMapper.toDto(savedEntity);
  }

  @Override
  public BackupDto update() {
    return null;
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
  public CursorPageResponseBackupDto searchBackupList(String worker, String status,
      String startedAtFrom, String startedAtTo, Long idAfter, String cursor, Integer size,
      String sortField, String sortDirection) {

    return null;
  }
}
