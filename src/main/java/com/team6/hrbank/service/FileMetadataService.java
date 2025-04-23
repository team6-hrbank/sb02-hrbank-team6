package com.team6.hrbank.service;

import com.team6.hrbank.entity.FileMetadata;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface FileMetadataService {

  Optional<FileMetadata> findById(Long id);
  FileMetadata create(MultipartFile file);

}
