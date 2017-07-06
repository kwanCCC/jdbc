package org.doraemon.treasure.grammer.mid.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class OrderByOperand implements Operand
{
  private final Operand operand;
  private final boolean desc;
}
