package org.doraemon.treasure.grammer.mid.model.operands.binary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.doraemon.treasure.grammer.mid.model.Operand;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public abstract class BinaryOperand implements Operand
{
  private final Operand left;
  private final Operand right;
  private final String  operator;
}
