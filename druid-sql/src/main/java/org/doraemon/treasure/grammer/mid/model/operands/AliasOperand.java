package org.doraemon.treasure.grammer.mid.model.operands;

import org.doraemon.treasure.grammer.mid.model.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class AliasOperand implements Operand
{
  private final Operand operand;
  private final String  alias;
}
