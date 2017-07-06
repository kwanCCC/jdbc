package org.doraemon.treasure.grammer.mid.model.operands;

import org.doraemon.treasure.grammer.mid.model.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class CountOperand implements Operand
{
  private final String  type;
  private final Operand name;
}
