package com.velocity.limit.service;

import com.velocity.limit.dto.DepositResponseDto;
import com.velocity.limit.dto.TransactionDto;
import com.velocity.limit.model.Transaction;
import com.velocity.limit.repository.TransactionRepository;
import com.velocity.limit.exception.ErrorCode;
import com.velocity.limit.model.ValidationResult;
import com.velocity.limit.validate.Validator;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VelocityLimitServiceImpl implements VelocityLimitService {

  private final Validator validator;
  private final TransactionRepository transactionRepository;

  @Override
  public DepositResponseDto deposit(TransactionDto transactionDto) {
    Transaction transaction = toEntity(transactionDto);
    log.info("Processing transaction with ID: {}, Customer ID: {}, Amount: {}",
        transactionDto.getId(), transactionDto.getCustomerId(), transactionDto.getLoadAmount());

    ValidationResult validationResult = validator.validate(transaction);

    if (validationResult.isAccepted()) {
      transaction.setIsAccepted(true);
      log.info("Transaction with ID: {} for Customer ID: {} is accepted", transaction.getTransactionId(), transaction.getCustomerId());
    } else {
      if (validationResult.getErrorCode().equals(ErrorCode.DUPLICATE_TRANSACTION)) {
        return null;
      }
    }
    transactionRepository.save(transaction);
    return new DepositResponseDto(transaction.getTransactionId(), transaction.getCustomerId(),
        transaction.getIsAccepted());
  }

  private Transaction toEntity(TransactionDto transactionDto) {
    String sanitizedAmount = transactionDto.getLoadAmount() != null ? transactionDto.getLoadAmount().replace("$", "").trim() : null;
    if (sanitizedAmount == null || sanitizedAmount.isEmpty()) {
      throw new IllegalArgumentException("Load amount cannot be null or empty");
    }
    return new Transaction(transactionDto.getId(), transactionDto.getCustomerId(),
        new BigDecimal(sanitizedAmount),
        transactionDto.getTime(), false);
  }

}
