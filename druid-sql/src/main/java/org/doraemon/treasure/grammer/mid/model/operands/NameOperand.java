package org.doraemon.treasure.grammer.mid.model.operands;

import org.doraemon.treasure.grammer.mid.model.Operand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class NameOperand implements Operand
{
  private final String table;
  private final String column;
}
