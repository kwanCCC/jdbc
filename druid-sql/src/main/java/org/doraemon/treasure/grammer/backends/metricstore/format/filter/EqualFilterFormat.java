package org.doraemon.treasure.grammer.backends.metricstore.format.filter;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ArithMetricColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.Column;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.ConstColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.columns.PrimitiveColumn;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.EqualFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.format.columns.ColumnFormat;
import org.doraemon.treasure.grammer.backends.metricstore.util.MetricStoreOperandSupport;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class EqualFilterFormat extends FilterFormat
{

  private static final ColumnFormat columnFormat = new ColumnFormat();

  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    BooleanExprEq equalExpr = (BooleanExprEq) expr;
    Operand left = equalExpr.getLeft();
    Operand right = equalExpr.getRight();

    if (left instanceof NameOperand && right instanceof PrimitiveOperand) {
      Column column = MetricStoreOperandSupport.buildPrimitiveColumn(
          ((NameOperand) left).getColumn(),
          ((PrimitiveOperand) right).getType()
      );
      return new EqualFilter(column, MetricStoreOperandSupport.getPrimitiveOperandValue((PrimitiveOperand) right));
    } else if (left instanceof PrimitiveOperand && right instanceof NameOperand) {
      Column column = MetricStoreOperandSupport.buildPrimitiveColumn(
          ((NameOperand) right).getColumn(),
          ((PrimitiveOperand) left).getType()
      );
      return new EqualFilter(column, MetricStoreOperandSupport.getPrimitiveOperandValue((PrimitiveOperand) left));
    } else if (left instanceof PrimitiveOperand && right instanceof PrimitiveOperand) {
      throw new SQLException("暂不支持两个常量的比较");
    } else if (left instanceof NameOperand && right instanceof NameOperand) {
      throw new SQLException("暂不支持两个列字段的比较");
    } else {
      Column leftColumn = columnFormat.format(equalExpr.getLeft());
      Column rightColumn = columnFormat.format(equalExpr.getRight());

      requireColumnType(leftColumn);
      requireColumnType(rightColumn);

      return new EqualFilter(leftColumn, rightColumn);
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
