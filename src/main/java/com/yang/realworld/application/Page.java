package com.yang.realworld.application;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class Page {

  private final int MAX_LIMITE = 100;
  private int offset;
  private int limit;

  public Page(int offset, int limit) {
    setLimit(limit);
    setLimit(offset);
  }

  public void setOffset(int offset) {
    if (offset > 0) {
      this.offset = offset;
    }
  }

  public void setLimit(int limit) {
    if (limit > MAX_LIMITE) {
      this.limit = MAX_LIMITE;
    } else if (limit > 0) {
      this.limit = limit;
    }
  }
}
