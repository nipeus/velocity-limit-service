package com.velocity.limit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.velocity.limit.dto.DepositResponseDto;
import com.velocity.limit.dto.TransactionDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration
class VelocityLimitServiceImplTest {

  @Autowired
  VelocityLimitService velocityLimitService;

  @Test
  void depositAmountBelowLimitAccepted() {
    TransactionDto transactionDto = new TransactionDto(1L, 1001L, "$1000.00", LocalDateTime.now());
    DepositResponseDto expectedResponse = new DepositResponseDto(1L, 1001L, false);
    DepositResponseDto response = velocityLimitService.deposit(transactionDto);

    assertNotNull(response);
    assertEquals(expectedResponse.getId(), response.getId());
    assertEquals(expectedResponse.getCustomerId(), response.getCustomerId());
    assertTrue(response.isAccepted());
  }

  @Test
  void depositAmount6kRejected() {
    TransactionDto transaction = new TransactionDto(6L, 1002L, "$6000.00", LocalDateTime.now());
    DepositResponseDto actualResponse = velocityLimitService.deposit(transaction);

    assertNotNull(actualResponse);
    assertEquals(6L, actualResponse.getId());
    assertEquals(1002L, actualResponse.getCustomerId());
    assertFalse(actualResponse.isAccepted());
  }

  @Test
  void depositAmount3kTwiceRejectedOnSecondAttempt() {
    TransactionDto firstTransaction = new TransactionDto(31L, 1003L, "$3000.00",
        LocalDateTime.now());
    DepositResponseDto firstResponse = velocityLimitService.deposit(firstTransaction);

    TransactionDto secondTransaction = new TransactionDto(32L, 1003L, "$3000.00",
        LocalDateTime.now());
    DepositResponseDto secondResponse = velocityLimitService.deposit(secondTransaction);

    assertNotNull(firstResponse);
    assertEquals(31L, firstResponse.getId());
    assertEquals(1003L, firstResponse.getCustomerId());
    assertTrue(firstResponse.isAccepted());

    assertNotNull(secondResponse);
    assertEquals(32L, secondResponse.getId());
    assertEquals(1003L, secondResponse.getCustomerId());
    assertFalse(secondResponse.isAccepted());
  }

  @Test
  void depositAmount400FourTimesRejectedOnFourthAttempt() {
    TransactionDto firstTransaction = new TransactionDto(1L, 1004L, "$400.00", LocalDateTime.now());
    DepositResponseDto firstResponse = velocityLimitService.deposit(firstTransaction);

    TransactionDto secondTransaction = new TransactionDto(2L, 1004L, "$400.00",
        LocalDateTime.now());
    DepositResponseDto secondResponse = velocityLimitService.deposit(secondTransaction);

    TransactionDto thirdTransaction = new TransactionDto(3L, 1004L, "$400.00", LocalDateTime.now());
    DepositResponseDto thirdResponse = velocityLimitService.deposit(thirdTransaction);

    TransactionDto fourthTransaction = new TransactionDto(4L, 1004L, "$400.00",
        LocalDateTime.now());
    DepositResponseDto fourthResponse = velocityLimitService.deposit(fourthTransaction);

    assertNotNull(firstResponse);
    assertTrue(firstResponse.isAccepted());

    assertNotNull(secondResponse);
    assertTrue(secondResponse.isAccepted());

    assertNotNull(thirdResponse);
    assertTrue(thirdResponse.isAccepted());

    assertNotNull(fourthResponse);
    assertFalse(fourthResponse.isAccepted());
  }

  @Test
  void invalidDepositAmountShouldThrowException() {
    assertThrows(IllegalArgumentException.class,
        () -> velocityLimitService.deposit(
            new TransactionDto(10L, 1004L, " ", LocalDateTime.now())),
        "Load amount cannot be null or empty");
  }

  @Test
  void depositAmount2kTwiceSameIdsIgnoredSecondAttempt() {
    TransactionDto firstTransaction = new TransactionDto(23L, 1005L, "$2000.00",
        LocalDateTime.now());
    DepositResponseDto firstResponse = velocityLimitService.deposit(firstTransaction);

    TransactionDto secondTransaction = new TransactionDto(23L, 1005L, "$2000.00",
        LocalDateTime.now());
    DepositResponseDto secondResponse = velocityLimitService.deposit(secondTransaction);

    assertNotNull(firstResponse);
    assertEquals(23L, firstResponse.getId());
    assertEquals(1005L, firstResponse.getCustomerId());
    assertTrue(firstResponse.isAccepted());

    assertNull(secondResponse);
  }


}