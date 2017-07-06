package org.doraemon.treasure.grammer.backends.druid.format.filter;

import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.SelectorFilter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class SelectorFilterFormat extends FilterFormat
{
  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    BooleanExprEq eqExpr = (BooleanExprEq) expr;
    Operand leftOperand = eqExpr.getLeft();
    Operand rightOperand = eqExpr.getRight();
    if (leftOperand instanceof NameOperand && rightOperand instanceof PrimitiveOperand) {
      return new SelectorFilter(((NameOperand) leftOperand).getColumn(), ((PrimitiveOperand) rightOperand).getValue());
    } else if (leftOperand instanceof PrimitiveOperand && rightOperand instanceof NameOperand) {
      return new SelectorFilter(((NameOperand) rightOperand).getColumn(), ((PrimitiveOperand) leftOperand).getValue());
    } else {
      throw new SQLException("'='运算符暂只支持<列名>=<值>");
    }
  }
}
