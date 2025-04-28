package com.team6.hrbank.swagger;

import com.team6.hrbank.entity.FileMetadata;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FileMetadata-Controller", description = "파일 메타데이터 및 파일 다운로드 관련 API")
public interface FileMetadataApi {

  @Operation(
      summary = "파일 다운로드",
      description = "파일 메타데이터 ID를 이용하여 파일을 다운로드합니다. 존재하지 않을 경우 404 에러를 반환합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
          @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음")
      }
  )
  ResponseEntity<Resource> downloadFile(
      @Parameter(
          description = "다운로드할 파일 메타데이터 ID",
          example = "1"
      )
      Long id
  );

  @Operation(
      summary = "파일 업로드 (테스트용)",
      description = "파일을 업로드하고, 파일 메타데이터를 저장합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "파일 업로드 및 저장 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
      }
  )
  ResponseEntity<FileMetadata> createFile(
      @Parameter(
          description = "업로드할 파일",
          required = true
      )
      MultipartFile multipartFile
  );

  @Operation(
      summary = "가장 최근 시간 조회 (테스트용)",
      description = "현재 시간 기준 하루 전 시간을 반환합니다. (단순 테스트용 API입니다.)",
      responses = {
          @ApiResponse(responseCode = "200", description = "최근 시간 조회 성공")
      }
  )
  ResponseEntity<Instant> getLatest();
}
