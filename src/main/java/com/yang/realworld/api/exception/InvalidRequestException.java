package com.yang.realworld.api.exception;

import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;

@NoArgsConstructor
public class InvalidRequestException extends RuntimeException {

  private  Errors errors;

  public InvalidRequestException(Errors errors) {
    super("");
    this.errors = errors;
  }


  public Errors getErrors(){
    return errors;
  }
}
