package org.doraemon.treasure.grammer.backends.druid.format.aggregators;


import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.MathAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.math.AggregationOperand;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MathAggregatorFormat extends AggregatorFormat
{
  private static final Map<String, String> AGG_DICT = new HashMap<>();

  static {
    AGG_DICT.put("longSum", "longSum");
    AGG_DICT.put("doubleSum", "doubleSum");
    AGG_DICT.put("longMin", "min");
    AGG_DICT.put("doubleMin", "doubleMin");
    AGG_DICT.put("longMax", "max");
    AGG_DICT.put("doubleMax", "doubleMax");
  }

  @Override
  public IAggregator format(Operand operand) throws SQLException
  {
    AggregationOperand aggOperand = (AggregationOperand) operand;

    String aggType = AGG_DICT.get(aggOperand.getType());

    if (aggType == null) {
      throw new SQLException("Cannot format Operand: " + aggOperand);
    }

    return new MathAggregator(aggType, null, aggOperand.getName().getColumn());
  }
}
