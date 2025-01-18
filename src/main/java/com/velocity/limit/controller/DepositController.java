package com.velocity.limit.controller;

import com.velocity.limit.dto.TransactionDto;
import com.velocity.limit.dto.DepositResponseDto;
import com.velocity.limit.service.VelocityLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepositController {

  @Autowired
  VelocityLimitService velocityLimitService;

  @PostMapping("/deposit")
  public DepositResponseDto deposit(@RequestBody @Validated TransactionDto transactionDto) {
    return velocityLimitService.deposit(transactionDto);
  }

}
