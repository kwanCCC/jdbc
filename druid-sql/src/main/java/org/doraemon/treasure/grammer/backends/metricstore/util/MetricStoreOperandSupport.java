package org.doraemon.treasure.grammer.backends.metricstore.util;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.DoubleColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.LongColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.primitives.StringColumn;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class MetricStoreOperandSupport
{

  public static Object getPrimitiveOperandValue(PrimitiveOperand operand) throws SQLException
  {
    Object result;
    switch (operand.getType()) {
      case "string":
        result = operand.getValue();
        break;
      case "long":
        result = Long.parseLong(operand.getValue());
        break;
      case "double":
        result = Double.parseDouble(operand.getValue());
        break;
      default:
        throw new SQLException("unsupported primitive type: " + operand.getType());
    }
    return result;
  }

  public static Column buildPrimitiveColumn(String name, String type) throws SQLException
  {
    Column result;
    switch (type) {
      case "string":
        result = new StringColumn(name);
        break;
      case "long":
        result = new LongColumn(name);
        break;
      case "double":
        result = new DoubleColumn(name);
        break;
      default:
        throw new SQLException("unsupported primitive type: " + type);
    }
    return result;
  }
}
