package com.velocity.limit.config;

import com.velocity.limit.repository.TransactionRepository;
import com.velocity.limit.validate.DayAmountTransactionValidator;
import com.velocity.limit.validate.DayCountTransactionValidator;
import com.velocity.limit.validate.UniqueTransactionValidator;
import com.velocity.limit.validate.Validator;
import com.velocity.limit.validate.WeekAmountTransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Value("${velocity.day.limit.count}")
  private Long dayLimitCount;

  @Value("${velocity.day.limit.amount}")
  private Double dayLimitAmount;

  @Value("${velocity.week.limit.amount}")
  private Double weekLimitAmount;

  @Autowired
  TransactionRepository transactionRepository;

  @Bean
  public Validator validator() {
    Validator validator = new UniqueTransactionValidator(transactionRepository);
    validator.linkWith(new DayCountTransactionValidator(dayLimitCount, transactionRepository))
        .linkWith(new DayAmountTransactionValidator(dayLimitAmount, transactionRepository))
        .linkWith(new WeekAmountTransactionValidator(weekLimitAmount, transactionRepository));
    return validator;
  }

}
