package com.velocity.limit.validate;

import com.velocity.limit.exception.ErrorCode;
import com.velocity.limit.model.Transaction;
import com.velocity.limit.model.ValidationResult;
import com.velocity.limit.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DayAmountTransactionValidator extends Validator {

  private final Double dayLimitAmount;
  private final TransactionRepository transactionRepository;

  @Override
  public ValidationResult validate(Transaction transaction) {
    List<Transaction> dailyTransactions = transactionRepository.findAllByCustomerIdAndTimeBetween(
        transaction.getCustomerId(), transaction.getTime().toLocalDate().atStartOfDay(),
        transaction.getTime().toLocalDate().atTime(
            LocalTime.MAX));
    BigDecimal totalAmount = dailyTransactions.stream()
        .filter(Transaction::getIsAccepted)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (totalAmount.add(transaction.getAmount()).doubleValue() > dayLimitAmount) {
      log.info(
          "Transaction with ID: {} for Customer ID: {} is rejected due to daily amount limit threshold",
          transaction.getTransactionId(), transaction.getCustomerId());
      return new ValidationResult(false, ErrorCode.DAY_LIMIT_AMOUNT);
    }
    return validateNext(transaction);
  }
}
