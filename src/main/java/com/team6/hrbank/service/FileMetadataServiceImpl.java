package com.team6.hrbank.service;

import com.team6.hrbank.entity.FileMetadata;
import com.team6.hrbank.exception.ErrorCode;
import com.team6.hrbank.exception.RestException;
import com.team6.hrbank.repository.FileMetadataRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileMetadataServiceImpl implements FileMetadataService{
  private final FileMetadataRepository fileMetadataRepository;

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Transactional
  @Override
  public Optional<FileMetadata> findById(Long id) {
    return fileMetadataRepository.findById(id);
  }

  @Transactional
  @Override
  public FileMetadata create(MultipartFile file) {
    try {
      // 1. 파일 메타 정보 추출
      String originalName = file.getOriginalFilename();
      String contentType = file.getContentType();
      int fileSize = (int) file.getSize();

      // 2. DB에 메타데이터 저장
      FileMetadata metadata = new FileMetadata(originalName, contentType, fileSize);
      fileMetadataRepository.save(metadata);

      // 3. 저장 경로 확인 및 없으면 생성
      Path uploadPath = Paths.get(uploadDir);
      if (Files.notExists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      // 4. 실제 파일 저장, 파일명 형식 (id + _ + originalName)
      String storedFileName = metadata.getId() + "_" + originalName;
      Path path = Paths.get(uploadDir, storedFileName);
      //multipart 객체를, getInputStream으로 파일 내용을 꺼내서, 지정한 경로에 복사(저장)
      Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

      return metadata;
    } catch (IOException e) {
      throw new RestException(ErrorCode.FILE_SAVE_FAILED);
    }
  }
}
