package org.doraemon.treasure.grammer.mid.model.operands.math;

import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ConditionAggregationOperand implements Operand
{
  private final Operand      innerOperand;
  private final IBooleanExpr condition;
}
