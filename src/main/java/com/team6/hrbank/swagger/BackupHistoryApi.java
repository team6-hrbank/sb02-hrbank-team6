package com.team6.hrbank.swagger;

import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Backup-Controller", description = "백업 이력 관련 API")
public interface BackupHistoryApi {

  @Operation(
      summary = "백업 생성",
      description = "새로운 백업을 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "백업 생성 성공"),
          @ApiResponse(responseCode = "500", description = "서버 오류 (백업 생성 실패)")
      }
  )
  ResponseEntity<BackupDto> createBackup();

  @Operation(
      summary = "가장 최근 백업 조회",
      description = "지정한 상태의 가장 최근 백업 이력을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "최근 백업 조회 성공"),
          @ApiResponse(responseCode = "404", description = "백업 이력을 찾을 수 없음")
      }
  )
  ResponseEntity<BackupDto> findLatest(
      @Parameter(
          description = "조회할 백업 상태 (예: COMPLETED, FAILED), 기본값: ",
          example = "COMPLETED",
          required = false
      )
      BackupStatus status
  );
}
