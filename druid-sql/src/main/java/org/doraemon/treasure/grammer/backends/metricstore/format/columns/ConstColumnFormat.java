package org.doraemon.treasure.grammer.backends.metricstore.format.columns;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import org.doraemon.treasure.grammer.backends.metricstore.util.MetricStoreOperandSupport;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class ConstColumnFormat extends ColumnFormat
{
  @Override
  public Column format(Operand operand) throws SQLException
  {
    PrimitiveOperand primitiveOperand = (PrimitiveOperand) operand;
    Object primitiveOperandValue = MetricStoreOperandSupport.getPrimitiveOperandValue(primitiveOperand);
    return new ConstColumn(primitiveOperandValue);
  }
}
