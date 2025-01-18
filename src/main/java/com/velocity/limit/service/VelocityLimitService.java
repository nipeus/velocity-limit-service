package com.velocity.limit.service;

import com.velocity.limit.dto.DepositResponseDto;
import com.velocity.limit.dto.TransactionDto;

public interface VelocityLimitService {

  DepositResponseDto deposit(TransactionDto transaction);

}
