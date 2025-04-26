package com.team6.hrbank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "change_log_details")
@NoArgsConstructor
public class ChangeLogDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "change_log_id", nullable = false)
  private ChangeLog changeLog;

  @Column(name = "property_name", nullable = false, length = 50)
  private String propertyName;

  @Column(name = "before_value")
  private String beforeValue;

  @Column(name = "after_value")
  private String afterValue;

  public ChangeLogDetail(String propertyName, String beforeValue, String afterValue) {
    if (propertyName == null || propertyName.isBlank()) {
      throw new IllegalArgumentException("변경된 속성명이 존재하지 않습니다.");
    }
    this.propertyName = propertyName;
    this.beforeValue = beforeValue;
    this.afterValue = afterValue;
  }

  public void setChangeLog(ChangeLog changeLog) {
    this.changeLog = changeLog;
  }


}
