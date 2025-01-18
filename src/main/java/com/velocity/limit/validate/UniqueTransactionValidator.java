package com.velocity.limit.validate;

import com.velocity.limit.exception.ErrorCode;
import com.velocity.limit.model.Transaction;
import com.velocity.limit.model.ValidationResult;
import com.velocity.limit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UniqueTransactionValidator extends Validator {

  private final TransactionRepository transactionRepository;


  @Override
  public ValidationResult validate(Transaction transaction) {

    if (transactionRepository.findByTransactionIdAndCustomerId(transaction.getTransactionId(), transaction.getCustomerId()).isPresent()) {
      log.info("Transaction with ID: {} for Customer ID: {} is rejected due to duplicate", transaction.getTransactionId(), transaction.getCustomerId());
      return new ValidationResult(false, ErrorCode.DUPLICATE_TRANSACTION);
    }
    return validateNext(transaction);
  }
}
