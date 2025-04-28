package com.team6.hrbank.swagger;

import com.team6.hrbank.dto.changeLog.ChangeLogSearchCondition;
import com.team6.hrbank.dto.changeLog.CursorPageResponseChangeLogDto;
import com.team6.hrbank.dto.changeLog.DiffDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "ChangeLog-Controller", description = "변경 이력 관련 API")
public interface ChangeLogApi {

  @Operation(
      summary = "변경 이력 목록 조회",
      description = "조건에 맞는 변경 이력 목록을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  ResponseEntity<CursorPageResponseChangeLogDto> getAllByCondition(
      @Parameter(
          description = "변경 이력 검색 조건 (예: ), 기본값: ",
          required = true
      )
      ChangeLogSearchCondition condition
  );

  @Operation(
      summary = "변경 이력 상세(diff) 조회",
      description = "특정 변경 이력의 상세 diff 목록을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "404", description = "변경 이력이 존재하지 않음")
      }
  )
  ResponseEntity<List<DiffDto>> getDetailsByChangeLogId(
      @Parameter(
          description = "변경 이력 ID",
          example = "123",
          required = true
      )
      Long changeLogId
  );

  @Operation(
      summary = "변경 이력 개수 조회",
      description = "특정 기간 동안의 변경 이력 개수를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  ResponseEntity<Long> getCount(
      @Parameter(
          description = "조회 시작 날짜 (UTC 기준 Instant)",
          example = "2024-01-01T00:00:00Z",
          schema = @Schema(type = "string", format = "date-time")
      )
      Instant fromDate,
      @Parameter(
          description = "조회 종료 날짜 (UTC 기준 Instant)",
          example = "2024-01-31T23:59:59Z",
          schema = @Schema(type = "string", format = "date-time")
      )
      Instant toDate
  );
}
