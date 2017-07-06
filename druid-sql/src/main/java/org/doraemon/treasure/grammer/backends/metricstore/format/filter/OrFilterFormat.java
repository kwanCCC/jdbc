package org.doraemon.treasure.grammer.backends.metricstore.format.filter;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.OrFilter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprOr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrFilterFormat extends FilterFormat
{
  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    BooleanExprOr orExpr = (BooleanExprOr) expr;
    List<IBooleanExpr> innerExpr = orExpr.getInnerExpr();

    List<Filter> innerFilter = new ArrayList<>(innerExpr.size());
    for (IBooleanExpr booleanExpr : innerExpr) {
      innerFilter.add(super.format(booleanExpr));
    }
    return new OrFilter(innerFilter);
  }
}
