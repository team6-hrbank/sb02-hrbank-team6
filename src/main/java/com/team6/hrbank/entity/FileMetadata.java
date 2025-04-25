package com.team6.hrbank.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_metadata")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadata {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "file_name", length = 32, nullable = false)
  private String fileName;

  @Column(name = "content_type", length = 50, nullable = false)
  private String contentType;

  @Column(name = "file_size", nullable = false)
  private int fileSize;

  public void update(String fileName, String contentType, int fileSize) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.fileSize = fileSize;
  }

}
