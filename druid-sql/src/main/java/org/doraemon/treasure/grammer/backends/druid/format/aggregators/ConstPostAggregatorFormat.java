package org.doraemon.treasure.grammer.backends.druid.format.aggregators;


import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.ConstPostAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class ConstPostAggregatorFormat extends AggregatorFormat
{
  @Override
  public IAggregator format(Operand operand) throws SQLException
  {
    PrimitiveOperand primitiveOperand = (PrimitiveOperand) operand;
    return new ConstPostAggregator(null, primitiveOperand.getValue());
  }
}
