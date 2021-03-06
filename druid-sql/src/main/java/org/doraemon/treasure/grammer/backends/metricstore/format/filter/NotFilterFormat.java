package org.doraemon.treasure.grammer.backends.metricstore.format.filter;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.NotFilter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprNot;

import java.sql.SQLException;

public class NotFilterFormat extends FilterFormat
{
  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    BooleanExprNot notExpr = (BooleanExprNot) expr;
    return new NotFilter(super.format(notExpr.getInner()));
  }
}
