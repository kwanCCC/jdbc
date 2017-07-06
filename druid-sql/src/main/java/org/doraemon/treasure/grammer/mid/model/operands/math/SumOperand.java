package org.doraemon.treasure.grammer.mid.model.operands.math;

import lombok.Data;
import lombok.Getter;

import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;

@Data
@Getter
public class SumOperand implements AggregationOperand
{
  private final String      type;
  private final NameOperand name;
}
