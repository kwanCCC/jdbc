package org.doraemon.treasure.grammer.mid.model.operands.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class MaxOperand implements AggregationOperand
{
  private final String      type;
  private final NameOperand name;
}
