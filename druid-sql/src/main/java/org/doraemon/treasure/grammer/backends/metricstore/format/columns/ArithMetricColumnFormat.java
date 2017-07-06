package org.doraemon.treasure.grammer.backends.metricstore.format.columns;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.AddColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.DivColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MinusColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.arithmetric.MultiplyColumn;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.AddOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.BinaryOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.DivideOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MinusOperand;
import org.doraemon.treasure.grammer.mid.model.operands.binary.MultiplyOperand;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ArithMetricColumnFormat extends ColumnFormat
{

  @Override
  public Column format(Operand operand) throws SQLException
  {
    BinaryOperand binaryOperand = (BinaryOperand) operand;
    List<Column> innerColumn = Arrays
        .asList(super.format(binaryOperand.getLeft()), super.format(binaryOperand.getRight()));

    return buildColumn(operand, binaryOperand, innerColumn);
  }

  @Override
  public Column formatWithRefColumn(List<String> aliasNames, Operand operand) throws SQLException {

    BinaryOperand binaryOperand = (BinaryOperand) operand;
    List<Column> innerColumn = Arrays
            .asList(super.formatWithRefColumn(aliasNames, binaryOperand.getLeft()),
                    super.formatWithRefColumn(aliasNames, binaryOperand.getRight()));

    return buildColumn(operand, binaryOperand, innerColumn);
  }

  private Column buildColumn(Operand operand, BinaryOperand binaryOperand, List<Column> innerColumn) throws SQLException {
    if (binaryOperand instanceof AddOperand) {
      return new AddColumn(innerColumn);
    } else if (binaryOperand instanceof DivideOperand) {
      return new DivColumn(innerColumn);
    } else if (binaryOperand instanceof MinusOperand) {
      return new MinusColumn(innerColumn);
    } else if (binaryOperand instanceof MultiplyOperand) {
      return new MultiplyColumn(innerColumn);
    } else {
      throw new SQLException("Cannot format Operand:" + operand);
    }
  }
}
