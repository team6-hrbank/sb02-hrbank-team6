package com.team6.hrbank.controller;

import com.team6.hrbank.entity.FileMetadata;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.service.FileMetadataService;
import com.team6.hrbank.swagger.FileMetadataApi;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileMetadataController implements FileMetadataApi {
  private final FileMetadataService fileMetadataService;

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Value("${file.backup-dir}")
  private String backupDir;

  @Value("${file.error-dir}")
  private String errorDir;

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) {
    // 존재하지 않으면 404
    FileMetadata metadata = fileMetadataService.findById(id)
        .orElseThrow(() -> new RestException(ErrorCode.FILE_NOT_FOUND));

    // 확장자에 따라 경로 결정
    String fileName = metadata.getFileName();
    String baseDir;

    if (fileName.endsWith(".csv")) {
      baseDir = backupDir;      // 백업 파일
    } else if (fileName.endsWith(".log")) {
      baseDir = errorDir;       // 에러 로그 파일
    } else {
      baseDir = uploadDir;      // 일반 업로드 파일 (이미지 등)
    }

    // 실제 파일 경로, 응답 포맷팅
    // 이미지를 다운로드 받을 일이 있나? 일단 킵
    Path path = Paths.get(baseDir, fileName);
    Resource resource = new FileSystemResource(path);

    // 파일이 실제로 존재하지 않는 경우 예외 처리
    if (!resource.exists()) {
      throw new RestException(ErrorCode.FILE_NOT_FOUND);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFileName() + "\"")
        .contentType(MediaType.parseMediaType(metadata.getContentType()))
        .body(resource);
  }

//  // 파일 생성 테스트용 API
//  @PostMapping("/create")
//  public ResponseEntity<FileMetadata> createFile(@RequestParam("file") MultipartFile multipartFile) {
//    FileMetadata saved = fileMetadataService.create(multipartFile);
//    return ResponseEntity.ok(saved);
//  }
//
//  // 테스트용
//  @GetMapping("/latest")
//  public ResponseEntity<Instant> getLatest() {
//    return ResponseEntity.ok(Instant.now().minus(Duration.ofDays(1)));
//  }
//
//  // 삭제 테스트용
//  @DeleteMapping("/{id}")
//  public void delete(@PathVariable Long id) {
//    System.out.println("삭제 요청");
//    fileMetadataService.deleteById(id);
//  }


}
