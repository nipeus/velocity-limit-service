package com.velocity.limit.validate;

import com.velocity.limit.model.Transaction;
import com.velocity.limit.model.ValidationResult;

public abstract class Validator {


  private Validator nextValidator;

  /**
   * Adds next validator to chain of responsibility
   *
   * @param next validator to be linked as next one
   * @return linked Validator
   */
  public Validator linkWith(Validator next) {
    this.nextValidator = next;
    return next;
  }

  /**
   * Performs chain of validations until linked validators remain
   */
  public abstract ValidationResult validate(Transaction transaction);

  protected ValidationResult validateNext(Transaction transaction) {
    if (nextValidator == null) {
      return new ValidationResult(true);
    }
    return nextValidator.validate(transaction);
  }

}
