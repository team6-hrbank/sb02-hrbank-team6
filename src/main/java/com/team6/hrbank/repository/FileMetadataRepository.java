package com.team6.hrbank.repository;

import com.team6.hrbank.entity.BackupHistories;
import com.team6.hrbank.entity.FileMetadata;

public interface FileMetadataRepository {
  FileMetadata findById(long id);

}
