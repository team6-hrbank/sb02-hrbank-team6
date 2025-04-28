package com.team6.hrbank.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table(name = "change_logs")
@NoArgsConstructor
public class ChangeLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "employee_id")
  private Employee employee;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChangeType type;

  private String memo;

  @Column(name = "ip_address", nullable = false, length = 20)
  private String ipAddress;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "changeLog", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChangeLogDetail> details = new ArrayList<>();

  public ChangeLog(Employee employee, ChangeType type, String memo, String ipAddress) {
    if (type == null) {
      throw new IllegalArgumentException("변경 유형 값이 존재하지 않습니다.");
    }
    if (ipAddress == null || ipAddress.isBlank()) {
      throw new IllegalArgumentException("IP 주소 값이 존재하지 않습니다.");
    }
    this.type = type;
    this.employee = employee;
    this.memo = memo;
    this.ipAddress = ipAddress;
  }

  public void addDetail(ChangeLogDetail detail) {
    this.details.add(detail);
    detail.setChangeLog(this);
  }

}
