package com.velocity.limit.validate;

import com.velocity.limit.exception.ErrorCode;
import com.velocity.limit.model.Transaction;
import com.velocity.limit.model.ValidationResult;
import com.velocity.limit.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WeekAmountTransactionValidator extends Validator {

  private final Double weekLimitAmount;
  private final TransactionRepository transactionRepository;


  @Override
  public ValidationResult validate(Transaction transaction) {
    LocalDate startOfWeek = transaction.getTime().toLocalDate().with(DayOfWeek.MONDAY);
    LocalDate endOfWeek = transaction.getTime().toLocalDate().with(DayOfWeek.SUNDAY);

    List<Transaction> dailyTransactions = transactionRepository.findAllByCustomerIdAndTimeBetween(
        transaction.getCustomerId(), startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX));
    BigDecimal totalAmount = dailyTransactions.stream()
        .filter(Transaction::getIsAccepted)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (totalAmount.add(transaction.getAmount()).doubleValue() > weekLimitAmount) {
      log.info(
          "Transaction with ID: {} for Customer ID: {} is rejected due to weekly amount limit threshold",
          transaction.getTransactionId(), transaction.getCustomerId());
      return new ValidationResult(false, ErrorCode.WEEK_LIMIT_AMOUNT);
    }
    return validateNext(transaction);
  }
}
