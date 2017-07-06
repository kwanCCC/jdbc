package org.doraemon.treasure.grammer.backends.druid.format.aggregators;


import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;

import java.sql.SQLException;

public class FieldAccessPostAggregatorFormat extends AggregatorFormat
{
  @Override
  public IAggregator format(Operand operand) throws SQLException
  {
    return super.format(operand);
  }
}
