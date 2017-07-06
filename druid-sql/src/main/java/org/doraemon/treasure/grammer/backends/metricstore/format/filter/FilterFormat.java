package org.doraemon.treasure.grammer.backends.metricstore.format.filter;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprEq;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprGt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLike;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprLt;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprNot;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;

import java.sql.SQLException;

public class FilterFormat
{
  private static final OrFilterFormat      orFilterFormat      = new OrFilterFormat();
  private static final AndFilterFormat     andFilterFormat     = new AndFilterFormat();
  private static final NotFilterFormat     notFilterFormat     = new NotFilterFormat();
  private static final LikeFilterFormat    likeFilterFormat    = new LikeFilterFormat();
  private static final EqualFilterFormat   equalFilterFormat   = new EqualFilterFormat();
  private static final CompareFilterFormat compareFilterFormat = new CompareFilterFormat();

  public Filter format(IBooleanExpr expr) throws SQLException
  {
    if (expr instanceof BooleanExprOr) {
      return orFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprAnd) {
      return andFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprNot) {
      return notFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprEq) {
      return equalFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprGt || expr instanceof BooleanExprLt) {
      return compareFilterFormat.format(expr);
    } else if (expr instanceof BooleanExprLike) {
      return likeFilterFormat.format(expr);
    } else {
      throw new SQLException("Cannot format BooleanExpr:" + expr);
    }
  }
}
