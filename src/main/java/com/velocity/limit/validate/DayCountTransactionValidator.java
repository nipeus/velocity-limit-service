package com.velocity.limit.validate;

import com.velocity.limit.exception.ErrorCode;
import com.velocity.limit.model.Transaction;
import com.velocity.limit.model.ValidationResult;
import com.velocity.limit.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DayCountTransactionValidator extends Validator {

  private final Long dayLimitCount;
  private final TransactionRepository transactionRepository;

  @Override
  public ValidationResult validate(Transaction transaction) {
    LocalDateTime time = transaction.getTime();

    List<Transaction> allByCustomerIdAndTimeBetween = transactionRepository.findAllByCustomerIdAndTimeBetween(
        transaction.getCustomerId(), time.toLocalDate().atStartOfDay(),
        time.toLocalDate().atTime(LocalTime.MAX));
    if (allByCustomerIdAndTimeBetween.size() >= dayLimitCount) {
      log.info(
          "Transaction with ID: {} for Customer ID: {} is rejected due to daily number limit threshold",
          transaction.getTransactionId(), transaction.getCustomerId());
      return new ValidationResult(false, ErrorCode.DAY_LIMIT_COUNT);
    }
    return validateNext(transaction);
  }
}

