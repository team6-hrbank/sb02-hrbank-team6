package com.team6.hrbank.mapper;

import com.team6.hrbank.dto.data.BackupDto;
import com.team6.hrbank.entity.BackupHistory;
import com.team6.hrbank.entity.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BackupMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "dto.worker", target = "operator")
  @Mapping(source = "fileMetadata", target = "backupFile")
  BackupHistory toEntity(BackupDto dto, FileMetadata fileMetadata);

  @Mapping(source = "operator", target = "worker")
  @Mapping(source = "backupFile.id", target = "fileId")
  BackupDto toDto(BackupHistory backupHistory);
}
