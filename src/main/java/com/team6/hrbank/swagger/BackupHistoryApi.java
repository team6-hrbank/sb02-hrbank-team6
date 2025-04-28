package com.team6.hrbank.swagger;

import com.team6.hrbank.dto.backup.CursorPageResponseBackupDto;
import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Backup-Controller", description = "데이터 백업 관련 API")
public interface BackupHistoryApi {

  @Operation(
      summary = "데이터 백업 목록 조회",
      description = "데이터 백업 목록을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "백업 목록 조회 성공"),
          @ApiResponse(responseCode = "500", description = "서버 오류 (백업 목록 조회 실패)")
      }
  )
  ResponseEntity<CursorPageResponseBackupDto> searchBackupList(
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String startedAtFrom,
      @RequestParam(required = false) String startedAtTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "startedAt") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      @RequestParam(defaultValue = "15") Integer size
  );

  @Operation(
      summary = "데이터 백업 생성",
      description = "데이터 백업을 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "백업 생성 성공"),
          @ApiResponse(responseCode = "500", description = "서버 오류 (백업 생성 실패)")
      }
  )
  ResponseEntity<BackupDto> createBackup(HttpServletRequest request);

  @Operation(
      summary = "최근 백업 이력 조회",
      description = "지정한 상태의 가장 최근 백업 이력을 조회합니다. 상태를 지정하지 않으면 성공적으로 완료된(COMPLETED) 백업을 반환합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "최근 백업 조회 성공"),
          @ApiResponse(responseCode = "404", description = "백업 이력을 찾을 수 없음")
      }
  )
  ResponseEntity<BackupDto> findLatest(
      @Parameter(
          description = "백업 상태 (COMPLETED, FAILED, IN_PROGRESS, 기본값: COMPLETED)",
          required = false
      )
      BackupStatus status
  );
}
