package org.doraemon.treasure.grammer.backends.metricstore.format.filter;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.AndFilter;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.filters.Filter;
import org.doraemon.treasure.grammer.mid.model.IBooleanExpr;
import org.doraemon.treasure.grammer.mid.model.booleanExprs.BooleanExprAnd;

import java.sql.SQLException;
import java.util.Arrays;

public class AndFilterFormat extends FilterFormat
{
  @Override
  public Filter format(IBooleanExpr expr) throws SQLException
  {
    BooleanExprAnd andExpr = (BooleanExprAnd) expr;
    return new AndFilter(Arrays.asList(super.format(andExpr.getLeft()), super.format(andExpr.getRight())));
  }
}
