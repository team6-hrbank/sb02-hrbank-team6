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

  @Column(name = "property_name", nullable = false)
  private String propertyName;

  @Column(name = "before_value")
  private String beforeValue;

  @Column(name = "after_value")
  private String afterValue;


}
