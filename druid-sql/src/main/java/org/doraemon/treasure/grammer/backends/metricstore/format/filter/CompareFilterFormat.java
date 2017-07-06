package org.doraemon.treasure.grammer.backends.metricstore.format.filter;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ArithMetricColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.PrimitiveColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.CompareFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.format.columns.ColumnFormat;
import org.doraemon.treasure.grammer.backends.metricstore.util.MetricStoreOperandSupport;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprGt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLt;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class CompareFilterFormat extends FilterFormat
{

  private static final ColumnFormat columnFormat = new ColumnFormat();

  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    Operand left;
    Operand right;
    String option;

    if (expr instanceof BooleanExprGt) {
      left = ((BooleanExprGt) expr).getLeft();
      right = ((BooleanExprGt) expr).getRight();
      option = ((BooleanExprGt) expr).getIsEquals() ? ">=" : ">";
    } else if (expr instanceof BooleanExprLt) {
      left = ((BooleanExprLt) expr).getLeft();
      right = ((BooleanExprLt) expr).getRight();
      option = ((BooleanExprLt) expr).getIsEquals() ? "<=" : "<";
    } else {
      throw new SQLException("Cannot format BooleanExpr:" + expr);
    }

    if (left instanceof NameOperand && right instanceof PrimitiveOperand) {
      Column column = MetricStoreOperandSupport
          .buildPrimitiveColumn(((NameOperand) left).getColumn(), ((PrimitiveOperand) right).getType());
      Object primitiveOperandValue = MetricStoreOperandSupport.getPrimitiveOperandValue((PrimitiveOperand) right);
      return new CompareFilter(column, new ConstColumn(primitiveOperandValue), option);
    } else if (left instanceof PrimitiveOperand && right instanceof NameOperand) {
      Column column = MetricStoreOperandSupport
          .buildPrimitiveColumn(((NameOperand) right).getColumn(), ((PrimitiveOperand) left).getType());
      Object primitiveOperandValue = MetricStoreOperandSupport.getPrimitiveOperandValue((PrimitiveOperand) left);
      return new CompareFilter(column, new ConstColumn(primitiveOperandValue), option);
    } else if (left instanceof PrimitiveOperand && right instanceof PrimitiveOperand) {
      throw new SQLException("暂不支持两个常量的比较");
    } else if (left instanceof NameOperand && right instanceof NameOperand) {
      throw new SQLException("暂不支持两个列字段的比较");
    } else {
      Column leftColumn = columnFormat.format(left);
      Column rightColumn = columnFormat.format(right);

      requireColumnType(leftColumn);
      requireColumnType(rightColumn);

      return new CompareFilter(leftColumn, rightColumn, option);
    }


  }


  private void requireColumnType(Column leftColumn) throws SQLException
  {

    if (leftColumn instanceof PrimitiveColumn
        || leftColumn instanceof ArithMetricColumn
        || leftColumn instanceof ConstColumn) {
      return;
    }
    throw new SQLException("无法解析的Column类型");
  }
}
