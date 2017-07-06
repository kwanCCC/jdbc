package org.doraemon.treasure.grammer.mid.model.operands.primitive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class StringPrimitiveOperand implements PrimitiveOperand
{
  private final String value;

  @Override
  public String getType()
  {
    return "string";
  }
}
