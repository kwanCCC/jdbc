package org.doraemon.treasure.grammer.backends.metricstore.format.columns;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.aggregations.ConditionColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.format.filter.FilterFormat;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.math.ConditionAggregationOperand;

import java.sql.SQLException;
import java.util.List;

public class ConditionColumnFormat extends ColumnFormat
{
  private static final FilterFormat filterFormat = new FilterFormat();

  @Override
  public Column format(Operand operand) throws SQLException
  {
    ConditionAggregationOperand aggOperand = (ConditionAggregationOperand) operand;
    Filter condition = filterFormat.format(aggOperand.getCondition());
    return new ConditionColumn(condition, super.format(aggOperand.getInnerOperand()));
  }

  @Override
  public Column formatWithRefColumn(List<String> aliasNames, Operand operand) throws SQLException {
    ConditionAggregationOperand aggOperand = (ConditionAggregationOperand) operand;
    Filter condition = filterFormat.format(aggOperand.getCondition());
    return new ConditionColumn(condition, super.formatWithRefColumn(aliasNames, aggOperand.getInnerOperand()));
  }

}
