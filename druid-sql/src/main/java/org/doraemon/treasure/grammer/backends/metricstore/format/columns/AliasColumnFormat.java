package org.doraemon.treasure.grammer.backends.metricstore.format.columns;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.others.AliasColumn;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.AliasOperand;

import java.sql.SQLException;
import java.util.List;

public class AliasColumnFormat extends ColumnFormat
{
  @Override
  public Column format(Operand operand) throws SQLException
  {
    AliasOperand aliasOperand = (AliasOperand) operand;
    return new AliasColumn(super.format(aliasOperand.getOperand()), aliasOperand.getAlias());
  }


  @Override
  public Column formatWithRefColumn(List<String> aliasNames, Operand operand) throws SQLException {
    AliasOperand aliasOperand = (AliasOperand) operand;
    return new AliasColumn(super.formatWithRefColumn(aliasNames, aliasOperand.getOperand()), aliasOperand.getAlias());
  }

}
