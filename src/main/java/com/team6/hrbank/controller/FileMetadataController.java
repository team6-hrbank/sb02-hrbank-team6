package com.team6.hrbank.controller;

import com.team6.hrbank.entity.FileMetadata;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.service.FileMetadataService;
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
public class FileMetadataController {
  private final FileMetadataService fileMetadataService;

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) {
    Optional<FileMetadata> optionalMetadata = fileMetadataService.findById(id);

    // 존재하지 않으면 404
    FileMetadata metadata = fileMetadataService.findById(id)
        .orElseThrow(() -> new RestException(ErrorCode.FILE_NOT_FOUND));

    // 실제 파일 경로, 응답 포맷팅
    Path path = Paths.get("test_fileDirs", metadata.getId() + "_" + metadata.getFileName());
    Resource resource = new FileSystemResource(path);

    // 파일이 실제로 존재하지 않는 경우도 예외 처리
    if (!resource.exists()) {
      throw new RestException(ErrorCode.FILE_NOT_FOUND);
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getFileName() + "\"")
        .contentType(MediaType.parseMediaType(metadata.getContentType()))
        .body(resource);
  }

  // 파일 생성 테스트용 API
  @PostMapping("/create")
  public ResponseEntity<FileMetadata>  createFile(@RequestParam("file") MultipartFile multipartFile) {
    FileMetadata saved = fileMetadataService.create(multipartFile);
    return ResponseEntity.ok(saved);
  }


}
