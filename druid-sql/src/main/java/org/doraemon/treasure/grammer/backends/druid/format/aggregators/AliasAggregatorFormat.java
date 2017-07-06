package org.doraemon.treasure.grammer.backends.druid.format.aggregators;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;

import java.sql.SQLException;

public class AliasAggregatorFormat extends AggregatorFormat
{
  @Override
  public IAggregator format(Operand operand) throws SQLException
  {
    AliasOperand aliasOperand = (AliasOperand) operand;
    IAggregator innerAggregator = super.format(aliasOperand.getOperand());
    if (innerAggregator == null) {
      return null;
    }
    innerAggregator.setName(aliasOperand.getAlias());
    return innerAggregator;
  }
}
