package com.velocity.limit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

@Component
public class TransactionApp implements CommandLineRunner {

  @Autowired
  private TransactionProcessor transactionProcessor;

  public static void main(String[] args) {
    SpringApplication.run(TransactionApp.class, args);
  }

  @Override
  public void run(String... args) {
    transactionProcessor.processTransactions();
  }
}
