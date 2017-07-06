package org.doraemon.treasure.grammer.backends.druid.format.filter;

import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprGt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprNot;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;

import java.sql.SQLException;

public class FilterFormat
{
  private static final NotFilterFormat notFilterFormat = new NotFilterFormat();
  private static final AndOrFilterFormat andOrFilterFormat = new AndOrFilterFormat();
  private static final BoundFilterFormat boundFilterFormat = new BoundFilterFormat();
  private static final SelectorFilterFormat selectorFilterFormat = new SelectorFilterFormat();


  public Filter format(IBooleanExpr expr) throws SQLException
  {
    if (expr == null) {
      throw new SQLException("Cannot format BooleanExpr:NULL");
    }

    if (expr instanceof BooleanExprNot) {
      return notFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprAnd || expr instanceof BooleanExprOr) {
      return andOrFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprGt || expr instanceof BooleanExprLt) {
      return boundFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprEq) {
      return selectorFilterFormat.format(expr);
    } else {
      throw new SQLException("Cannot Format BooleanExpr:" + expr);
    }
  }
}
