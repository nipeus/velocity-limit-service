package com.velocity.limit.repository;

import com.velocity.limit.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Optional<Transaction> findByTransactionIdAndCustomerId(Long transactionId, Long customerId);

  List<Transaction> findAllByCustomerIdAndTimeBetween(Long customerId, LocalDateTime start, LocalDateTime end);
}
