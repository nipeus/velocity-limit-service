package com.velocity.limit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name= "transaction_id", nullable = false)
  private Long transactionId;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @Column(name = "load_amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "time", nullable = false)
  private LocalDateTime time;

  @Column(name = "is_accepted", nullable = false)
  private Boolean isAccepted;

  public Transaction(Long transactionId, Long customerId, BigDecimal amount, LocalDateTime time,
      Boolean isAccepted) {
    this.transactionId = transactionId;
    this.customerId = customerId;
    this.amount = amount;
    this.time = time;
    this.isAccepted = isAccepted;
  }
}
