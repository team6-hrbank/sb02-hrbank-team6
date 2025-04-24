package com.team6.hrbank.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "backup_histories")
@Getter
@NoArgsConstructor
public class BackupHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "operator", length = 15, nullable = false)
  private String operator;

  @Column(name = "started_at", nullable = false)
  private Instant startedAt;

  @Column(name = "ended_at")
  private Instant endedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private BackupStatus status;

  @OneToOne(optional = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "backup_file_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "backup_file_fk"), nullable = true)
  private FileMetadata backupFile;

  public BackupHistory(FileMetadata backupFile, String operator,
      Instant startedAt, Instant endedAt, BackupStatus status) {
    this.backupFile = backupFile;
    this.operator = operator;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.status = status;
  }
}
