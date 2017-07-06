package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import org.doraemon.treasure.grammer.backends.checker.SelectChecker;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.SelectQuery;
import org.doraemon.treasure.grammer.parser.Query;
import org.joda.time.Interval;

import java.sql.SQLException;

public class SelectQueryCompiler extends IMetricStoreQueryCompiler<SelectQuery>
{

  @Override
  public boolean canCompile(Query query)
  {
    return SelectChecker.isConditionMet(query);
  }

  @Override
  protected SelectQuery buildConcreteQuery(Query query) throws SQLException
  {
    Interval interval = new Interval(query.getTimestamps().getLeft(), query.getTimestamps().getRight());
    SelectQuery.SelectQueryBuilder builder = SelectQuery.builder();
    builder.type("select");
    builder.dataSource(query.getTable());
    builder.interval(interval);
    builder.columns(convertOperandsToColumns(query.getColumns()));
    builder.filter(convertBooleanExprToFilter(query.getWhereClause()));
    builder.limit(convertLimit(query.getLimit(), query.getOrderBy(), query.getColumns()));
    builder.isUnique(query.getIsUnique() == null ? false : query.getIsUnique());
    return builder.build();
  }

}
