package com.team6.hrbank.repository;

import com.team6.hrbank.entity.FileMetadata;
import java.util.Optional;

public interface FileMetadataRepository {
  Optional<FileMetadata> findById(Long id);

}
