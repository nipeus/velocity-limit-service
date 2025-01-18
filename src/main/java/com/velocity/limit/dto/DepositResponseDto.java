package com.velocity.limit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class DepositResponseDto {

  @JsonSerialize(using = ToStringSerializer.class)
  private long id;

  @JsonProperty("customer_id")
  @JsonSerialize(using = ToStringSerializer.class)
  private long customerId;

  @JsonProperty("accepted")
  private boolean isAccepted;

  public DepositResponseDto(long id, long customerId, boolean isAccepted) {
    this.id = id;
    this.customerId = customerId;
    this.isAccepted = isAccepted;
  }
}
