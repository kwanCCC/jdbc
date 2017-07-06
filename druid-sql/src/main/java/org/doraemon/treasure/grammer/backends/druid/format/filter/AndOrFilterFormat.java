package org.doraemon.treasure.grammer.backends.druid.format.filter;

import org.doraemon.treasure.grammer.backends.druid.dsl.filters.AndOrFilter;
import org.doraemon.treasure.grammer.backends.druid.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AndOrFilterFormat extends FilterFormat
{
  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    String option;

    List<IBooleanExpr> innerExpr = new ArrayList<>();

    if (expr instanceof BooleanExprAnd) {
      BooleanExprAnd andExpr = (BooleanExprAnd) expr;
      option = "and";
      innerExpr.add(andExpr.getLeft());
      innerExpr.add(andExpr.getRight());
    } else if (expr instanceof BooleanExprOr) {
      BooleanExprOr orExpr = (BooleanExprOr) expr;
      option = "or";
      innerExpr = orExpr.getInnerExpr();
    } else {
      throw new SQLException("Cannot Format BooleanExpr:" + expr);
    }

    List<Filter> filters = new ArrayList<>(innerExpr.size());
    for (IBooleanExpr booleanExpr : innerExpr) {
      filters.add(super.format(booleanExpr));
    }
    return new AndOrFilter(option, filters);
  }
}
