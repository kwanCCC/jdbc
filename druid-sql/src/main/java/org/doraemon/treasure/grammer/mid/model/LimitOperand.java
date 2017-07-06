package org.doraemon.treasure.grammer.mid.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class LimitOperand implements Operand
{
  private final int offset;
  private final int resultCount;

  public int getMaxSize()
  {
    return offset + resultCount;
  }

}
