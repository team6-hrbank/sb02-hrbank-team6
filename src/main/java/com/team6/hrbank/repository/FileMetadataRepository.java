package com.team6.hrbank.repository;

import com.team6.hrbank.entity.FileMetadata;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

}

