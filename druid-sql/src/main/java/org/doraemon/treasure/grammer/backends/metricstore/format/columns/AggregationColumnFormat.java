package org.doraemon.treasure.grammer.backends.metricstore.format.columns;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MaxColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.MinColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.SumColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.LongColumn;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.math.AggregationOperand;

import java.sql.SQLException;
import java.util.List;

public class AggregationColumnFormat extends ColumnFormat
{
  @Override
  public Column format(Operand operand) throws SQLException
  {
    AggregationOperand aggregationOperand = (AggregationOperand) operand;

    String aggType   = aggregationOperand.getType();
    String fieldName = aggregationOperand.getName().getColumn();

    Column withTypeColumn = createWithTypeColumn(aggregationOperand, aggType, fieldName);

    if (aggType.endsWith("Sum")) {
      return new SumColumn(withTypeColumn);
    } else if (aggType.endsWith("Max")) {
      return new MaxColumn(withTypeColumn);
    } else if (aggType.endsWith("Min")) {
      return new MinColumn(withTypeColumn);
    } else {
      throw new SQLException("Cannot format Operand: " + aggregationOperand);
    }
  }

  private Column createWithTypeColumn(AggregationOperand aggregationOperand, String aggType, String fieldName) throws SQLException {
    if (aggType.startsWith("long")) {
      return new LongColumn(fieldName);
    } else if (aggType.startsWith("double")) {
      return new DoubleColumn(fieldName);
    } else {
      throw new SQLException("Cannot format Operand: " + aggregationOperand);
    }
  }

  @Override
  public Column formatWithRefColumn(List<String> aliasNames, Operand operand) throws SQLException {
     return format(operand);
  }
}
