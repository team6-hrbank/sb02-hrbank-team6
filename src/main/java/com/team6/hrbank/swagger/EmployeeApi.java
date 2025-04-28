package com.team6.hrbank.swagger;

import com.team6.hrbank.dto.employee.CursorPageResponseEmployeeDto;
import com.team6.hrbank.dto.employee.EmployeeCreateRequest;
import com.team6.hrbank.dto.employee.EmployeeDto;
import com.team6.hrbank.dto.employee.EmployeeSearchCondition;
import com.team6.hrbank.dto.employee.EmployeeUpdateRequest;
import com.team6.hrbank.entity.EmployeeState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Employee-Controller", description = "직원 관련 API")
public interface EmployeeApi {

  @Operation(
      summary = "직원 생성",
      description = "신규 직원을 생성합니다. 직원 정보와 프로필 이미지를 함께 업로드할 수 있습니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "직원 생성 성공"),
          @ApiResponse(responseCode = "400", description = "요청 값이 잘못됨")
      }
  )
  ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(
          description = "직원 생성 요청 정보",
          required = true
      )
      EmployeeCreateRequest request,

      @Parameter(
          description = "프로필 이미지 파일 (선택)"
      )
      MultipartFile profileImage,
      HttpServletRequest httpRequest
  );

  @Operation(
      summary = "직원 단건 조회",
      description = "ID를 기반으로 직원 정보를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "직원 조회 성공"),
          @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음")
      }
  )
  ResponseEntity<EmployeeDto> findByEmployeeId(
      @Parameter(
          description = "직원 ID",
          example = "1",
          required = true
      )
      Long id
  );

  @Operation(
      summary = "직원 목록 검색",
      description = "검색 조건에 맞는 직원 목록을 커서 기반으로 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  CursorPageResponseEmployeeDto searchEmployees(
      @Parameter(
          description = "직원 검색 조건, (예: ) 기본값: ",
          required = false
      )
      EmployeeSearchCondition condition
  );

  @Operation(
      summary = "직원 정보 수정",
      description = "ID를 기반으로 직원 정보를 수정합니다. 직원 정보와 프로필 이미지를 함께 수정할 수 있습니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "직원 수정 성공"),
          @ApiResponse(responseCode = "400", description = "요청 값이 잘못됨"),
          @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음")
      }
  )
  ResponseEntity<EmployeeDto> updateEmployee(
      @Parameter(
          description = "수정할 직원 ID",
          example = "1",
          required = true
      )
      Long id,

      @Parameter(
          description = "직원 수정 요청 정보",
          required = true
      )
      EmployeeUpdateRequest request,

      @Parameter(
          description = "프로필 이미지 파일 (선택)"
      )
      @RequestPart(value = "profile", required = false) MultipartFile profileImage,

      HttpServletRequest httpRequest
  );

  @Operation(
      summary = "직원 삭제",
      description = "ID를 기반으로 직원을 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "204", description = "직원 삭제 성공"),
          @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음")
      }
  )
  ResponseEntity<EmployeeDto> deleteEmployee(
      @Parameter(
          description = "삭제할 직원 ID",
          example = "1",
          required = true
      )
      Long id,

      HttpServletRequest httpRequest
  );

  @Operation(
      summary = "직원 수 조회",
      description = "상태 및 기간에 따라 직원 수를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "직원 수 조회 성공")
      }
  )
  ResponseEntity<Long> countEmployee(
      @Parameter(
          description = "직원 상태 (예: ACTIVE, ON_LEAVE, RESIGNED), 기본값: ",
          example = "ACTIVE",
          required = false
      )
      EmployeeState status,

      @Parameter(
          description = "조회 시작일 (yyyy-MM-dd), 기본값: ",
          example = "2024-01-01",
          required = false,
          schema = @Schema(type = "string", format = "date")
      )
      LocalDate fromDate,

      @Parameter(
          description = "조회 종료일 (yyyy-MM-dd), 기본값: ",
          example = "2024-03-31",
          required = false,
          schema = @Schema(type = "string", format = "date")
      )
      LocalDate toDate
  );
}