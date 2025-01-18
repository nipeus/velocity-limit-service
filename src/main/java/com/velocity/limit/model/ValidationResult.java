package com.velocity.limit.model;

import com.velocity.limit.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationResult {


  private final boolean isAccepted;
  private ErrorCode errorCode;

  public ValidationResult(boolean isAccepted) {
    this.isAccepted = isAccepted;
  }

}
