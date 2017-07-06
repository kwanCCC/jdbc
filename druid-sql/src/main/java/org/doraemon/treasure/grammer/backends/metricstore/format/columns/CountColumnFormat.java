package org.doraemon.treasure.grammer.backends.metricstore.format.columns;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.CountColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.DistinctCountColumn;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.CountOperand;
import org.doraemon.treasure.grammer.mid.model.operands.DistinctOperand;

import java.sql.SQLException;
import java.util.List;

public class CountColumnFormat extends ColumnFormat
{
  @Override
  public Column format(Operand operand) throws SQLException
  {
    Operand nameOperand = ((CountOperand) operand).getName();
    if (nameOperand != null && nameOperand instanceof DistinctOperand) {
      Operand innerOperand = ((DistinctOperand) nameOperand).getInnerOperand();
      return new DistinctCountColumn(super.format(innerOperand));
    } else {
      return new CountColumn();
    }
  }

  @Override
  public Column formatWithRefColumn(List<String> aliasNames, Operand operand) throws SQLException
  {
    Operand nameOperand = ((CountOperand) operand).getName();
    if (nameOperand != null && nameOperand instanceof DistinctOperand) {
      Operand innerOperand = ((DistinctOperand) nameOperand).getInnerOperand();
      return new DistinctCountColumn(super.formatWithRefColumn(aliasNames, innerOperand));
    } else {
      return new CountColumn();
    }
  }
}
