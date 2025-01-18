package com.velocity.limit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransactionDto {

  Long id;

  @JsonProperty("customer_id")
  @JsonSerialize(using = ToStringSerializer.class)
  Long customerId;

  @JsonProperty("load_amount")
  String loadAmount;

  LocalDateTime time;
}
