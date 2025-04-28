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

@Tag(name = "ChangeLog-Controller", description = "직원 정보 변경 이력 관리 API")
public interface ChangeLogApi {

  @Operation(
      summary = "직원 정보 변경 이력 목록 조회",
      description = "검색 조건에 맞는 변경 이력 목록을 커서 기반으로 조회합니다. 상세 변경 내용은 포함되지 않습니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  ResponseEntity<CursorPageResponseChangeLogDto> getAllByCondition(
      @Parameter(
          description = "변경 이력 검색 조건: 직원 사번, 이력 유형, 내용, IP 주소, 수정 일시 기간\n" +
                        "커서 조건: 이전 페이지 마지막 요소의 ID, 커서(이전 페이지 마지막 정렬 기준 값), 페이지 크기\n" +
                        "정렬 기준: 생성 일시, IP 주소"
      )
      ChangeLogSearchCondition condition
  );

  @Operation(
      summary = "변경 이력의 상세 내용 조회",
      description = "변경 이력의 상세 내용을 목록으로 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "404", description = "수정 이력 상세 내용을 찾을 수 없습니다.")
      }
  )
  ResponseEntity<List<DiffDto>> getDetailsByChangeLogId(
      @Parameter(
          description = "변경 이력의 ID",
          example = "123",
          required = true
      )
      Long changeLogId
  );

  @Operation(
      summary = "변경 이력 건수 조회",
      description = "변경 이력 건수를 조회합니다. 조회 시작일과 종료일을 파라미터로 제공하지 않으면 최근 일주일 데이터를 반환합니다.",
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
