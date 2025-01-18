package com.velocity.limit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.velocity.limit.dto.TransactionDto;
import com.velocity.limit.dto.DepositResponseDto;
import com.velocity.limit.service.VelocityLimitService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionProcessor {

  @Value("${app.input.file}")
  private String inputFile;

  @Value("${app.output.file}")
  private String outputFile;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private VelocityLimitService velocityLimitService;

  public void processTransactions() {
    List<TransactionDto> transactions = new ArrayList<>();
    objectMapper.registerModule(new JavaTimeModule());
    try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        TransactionDto transaction = objectMapper.readValue(line, TransactionDto.class);
        transactions.add(transaction);
      }
    } catch (IOException e) {
      log.error("Error reading input file: {}", e.getMessage());
      return;
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
      for (TransactionDto transactionDto : transactions) {
        DepositResponseDto response = velocityLimitService.deposit(transactionDto);
        if (Objects.nonNull(response)) {
          String resultJson = objectMapper.writeValueAsString(response);
          bw.write(resultJson);
          bw.newLine();
        }
      }
    } catch (IOException e) {
      log.error("Error writing output file: {}", e.getMessage());
    }
    log.info("Input file processing completed. Results saved to: {}", outputFile);
  }
}
