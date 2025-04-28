package com.team6.hrbank.swagger;

import com.team6.hrbank.dto.employeestats.EmployeeDistributionDto;
import com.team6.hrbank.dto.employeestats.EmployeeTrendDto;
import com.team6.hrbank.entity.EmployeeState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "DashBoard-Controller", description = "대시보드 관련 API")
public interface DashBoardApi {
  @Operation(summary = "직원 수 전체 추이 조회",
      description = "일별 / 주별 / 월별 / 분기별 / 년도별로 직원 수 추이를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      })
  ResponseEntity<List<EmployeeTrendDto>> getEmployeeTrend(@Parameter(
      description = "조회 시작일 (yyyy-MM-dd), 기본값: 현재 날짜 - 12Unit",
      example = "2024-01-01",
      schema = @Schema(type = "string", format = "date")
  ) LocalDate from,
      @Parameter(
          description = "조회 종료일 (yyyy-MM-dd), 기본값: 현재 날짜",
          example = "2024-03-31",
          schema = @Schema(type = "string", format = "date")
      ) LocalDate to,
      @Parameter(
          description = "집계 단위 (MONTH, DAY, QUARTER, YEAR, WEEK), 기본값: MONTH",
          example = "MONTH",
          schema = @Schema(type = "string"))
      String unit);

  @Operation(summary = "직무/부서 별 직원 분포 조회",
      description = "직무/부서 별 직원 분포를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "400", description = "조회 실패: 잘못된 파라미터 입력"),
          @ApiResponse(responseCode = "404", description = "조회 실패: 오늘/어제자 직원 수 추이 데이터가 존재하지 않음")
      })
  ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
      @Parameter(
          description = "그룹핑 기준 (예: department, position), 기본값: department",
          example = "department",
          schema = @Schema(
              type = "string"
          )
      )
      String groupBy,
      @Parameter(
          description = "직원 상태 (예: ACTIVE, ON_LEAVE, RESIGNED), 기본값: ACTIVE",
          example = "ACTIVE",
          schema = @Schema(
              type = "string",
              allowableValues = {"ACTIVE", "ON_LEAVE", "RESIGNED"}
          )
      )
      EmployeeState state);
}
