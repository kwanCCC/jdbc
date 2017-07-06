package org.doraemon.treasure.grammer.backends.druid.format.aggregators;


import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.ArithMetricPostAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.BinaryOperand;

import java.sql.SQLException;
import java.util.Arrays;

public class ArithMetricPostAggregatorFormat extends AggregatorFormat
{
  @Override
  public IAggregator format(Operand operand) throws SQLException
  {
    BinaryOperand binaryOperand = (BinaryOperand) operand;
    return new ArithMetricPostAggregator(
        null,
        binaryOperand.getOperator(),
        Arrays.asList(super.format(binaryOperand.getLeft()), super.format(binaryOperand.getRight()))
    );
  }
}
