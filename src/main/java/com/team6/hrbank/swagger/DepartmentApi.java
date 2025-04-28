package com.team6.hrbank.swagger;

import com.team6.hrbank.dto.department.CursorPageResponseDepartmentDto;
import com.team6.hrbank.dto.department.DepartmentCreateRequest;
import com.team6.hrbank.dto.department.DepartmentDto;
import com.team6.hrbank.dto.department.DepartmentSearchCondition;
import com.team6.hrbank.dto.department.DepartmentUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Department-Controller", description = "부서 관련 API")
public interface DepartmentApi {

  @Operation(
      summary = "부서 생성",
      description = "새로운 부서를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "201", description = "부서 생성 성공"),
          @ApiResponse(responseCode = "400", description = "요청 값이 잘못됨")
      }
  )
  ResponseEntity<DepartmentDto> create(
      @Parameter(
          description = "생성할 부서 정보",
          required = true
      )
      DepartmentCreateRequest request
  );

  @Operation(
      summary = "부서 조회",
      description = "ID를 기반으로 부서 정보를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "부서 조회 성공"),
          @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
      }
  )
  ResponseEntity<DepartmentDto> find(
      @Parameter(
          description = "조회할 부서 ID",
          example = "1",
          required = true
      )
      Long id
  );

  @Operation(
      summary = "부서 수정",
      description = "ID를 기반으로 부서 정보를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "부서 수정 성공"),
          @ApiResponse(responseCode = "400", description = "요청 값이 잘못됨"),
          @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
      }
  )
  ResponseEntity<DepartmentDto> update(
      @Parameter(
          description = "수정할 부서 ID",
          example = "1",
          required = true
      )
      Long id,
      @Parameter(
          description = "수정할 부서 정보",
          required = true
      )
      DepartmentUpdateRequest request
  );

  @Operation(
      summary = "부서 삭제",
      description = "ID를 기반으로 부서를 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "204", description = "부서 삭제 성공"),
          @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음")
      }
  )
  ResponseEntity<Void> delete(
      @Parameter(
          description = "삭제할 부서 ID",
          example = "1",
          required = true
      )
      Long id
  );

  @Operation(
      summary = "부서 목록 조회",
      description = "검색 조건에 맞는 부서 목록을 커서 기반으로 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회 성공")
      }
  )
  ResponseEntity<CursorPageResponseDepartmentDto> getDepartments(
      @Parameter(
          description = "부서 검색 조건, (예: ), 기본값: ",
          required = false
      )
      DepartmentSearchCondition condition
  );
}
