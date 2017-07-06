package org.doraemon.treasure.grammer.backends.druid.format.aggregators;


import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.CountAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.aggregators.IAggregator;
import org.doraemon.treasure.grammer.backends.druid.dsl.postAggregators.HyperUniquePostAggregator;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.DistinctOperand;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;

import java.sql.SQLException;

public class CountAggregatorFormat extends AggregatorFormat
{

  @Override
  public IAggregator format(Operand operand) throws SQLException
  {
    Operand nameOperand = ((CountOperand) operand).getName();
    if (nameOperand != null && nameOperand instanceof DistinctOperand) {
      Operand innerOperand = ((DistinctOperand) nameOperand).getInnerOperand();
      if (innerOperand instanceof NameOperand) {
        return new HyperUniquePostAggregator(null, ((NameOperand) innerOperand).getColumn());
      } else {
        throw new SQLException("暂只支持Count(DISTINCT <字段>)");
      }
    } else {
      return new CountAggregator();
    }
  }
}
