package org.doraemon.treasure.grammer.backends.druid.format.aggregators;

import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;
import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.BinaryOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.AggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.math.ConditionAggregationOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class AggregatorFormat
{

  private static final MathAggregatorFormat            mathAggregatorFormat                = new MathAggregatorFormat();
  private static final AliasAggregatorFormat           aliasAggregatorFormat               = new AliasAggregatorFormat();
  private static final CountAggregatorFormat           countAggregatorFormat               = new CountAggregatorFormat();
  private static final ConstPostAggregatorFormat       constPostAggregatorFormat           = new ConstPostAggregatorFormat();
  private static final ConditionAggregatorFormat       conditionAggregatorFormat           = new ConditionAggregatorFormat();
  private static final ArithMetricPostAggregatorFormat ARITH_METRIC_POST_AGGREGATOR_FORMAT = new ArithMetricPostAggregatorFormat();

  public IAggregator format(Operand operand) throws SQLException
  {
    if (operand == null) {
      throw new SQLException("不能解析的Operand:NULL");
    }

    if (operand instanceof CountOperand) {
      return countAggregatorFormat.format(operand);
    } else if (operand instanceof PrimitiveOperand) {
      return constPostAggregatorFormat.format(operand);
    } else if (operand instanceof BinaryOperand) {
      return ARITH_METRIC_POST_AGGREGATOR_FORMAT.format(operand);
    } else if (operand instanceof AggregationOperand) {
      return mathAggregatorFormat.format(operand);
    } else if (operand instanceof AliasOperand) {
      return aliasAggregatorFormat.format(operand);
    } else if (operand instanceof ConditionAggregationOperand) {
      return conditionAggregatorFormat.format(operand);
    } else if (operand instanceof NameOperand) {
      return null;
    } else {
      throw new SQLException("Cannot format Operand:" + operand);
    }
  }
}
