package org.doraemon.treasure.grammer.backends.druid.format.filter;


import org.doraemon.treasure.grammer.backends.druid.dsl.filters.BoundFilter;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.Operand;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprGt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLt;
import org.doraemon.treasure.grammer.mid.model.operands.NameOperand;
import org.doraemon.treasure.grammer.mid.model.operands.primitive.PrimitiveOperand;

import java.sql.SQLException;

public class BoundFilterFormat extends FilterFormat
{
  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    BoundFilter.BoundFilterBuilder builder = BoundFilter.builder();
    if (expr instanceof BooleanExprGt) {
      Operand left = ((BooleanExprGt) expr).getLeft();
      Operand right = ((BooleanExprGt) expr).getRight();
      builder
          .alphaNumeric(true)
          .dimension(getNameOperand(left, right).getColumn())
          .lower(getPrimitiveOperand(left, right).getValue())
          .lowerStrict(((BooleanExprGt) expr).getIsEquals());
    } else if (expr instanceof BooleanExprLt) {
      Operand left = ((BooleanExprLt) expr).getLeft();
      Operand right = ((BooleanExprLt) expr).getRight();
      builder
          .alphaNumeric(true)
          .dimension(getNameOperand(left, right).getColumn())
          .upper(getPrimitiveOperand(left, right).getValue())
          .upperStrict(((BooleanExprLt) expr).getIsEquals());
    } else {
      throw new SQLException("Cannot format BooleanExpr:" + expr);
    }
    return builder.build();
  }

  private PrimitiveOperand getPrimitiveOperand(Operand left, Operand right)
  {
    return (PrimitiveOperand) (PrimitiveOperand.class.isInstance(left) ? left : right);
  }

  private NameOperand getNameOperand(Operand left, Operand right)
  {
    return (NameOperand) (NameOperand.class.isInstance(left) ? left : right);
  }
}
