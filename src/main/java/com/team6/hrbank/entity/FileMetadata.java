package com.team6.hrbank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "file_metadata")
public class FileMetadata implements Serializable {
  @Id
  private static final long serialVersionUID = 1L;
  private String file_name;
  private String content_type;
  private int file_size;

}
