package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import org.doraemon.treasure.grammer.backends.checker.TopNChecker;
import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.TopNQuery;
import org.doraemon.treasure.grammer.parser.Query;
import org.joda.time.Interval;

import java.sql.SQLException;

public class TopNQueryCompiler extends IMetricStoreQueryCompiler<TopNQuery>
{

  @Override
  public boolean canCompile(Query query) throws SQLException
  {
    return TopNChecker.isConditionMet(query);
  }

  @Override
  protected TopNQuery buildConcreteQuery(Query query) throws SQLException
  {
    Interval interval = new Interval(query.getTimestamps().getLeft(), query.getTimestamps().getRight());
    TopNQuery.TopNQueryBuilder builder = TopNQuery.builder();
    builder.type("topn");
    builder.dataSource(query.getTable());
    builder.interval(interval);
    builder.columns(convertOperandsToColumns(query.getColumns()));
    builder.filter(convertBooleanExprToFilter(query.getWhereClause()));
    builder.groupBys(convertGroupBys(query.getGroupBys()));
    builder.limit(convertLimit(query.getLimit(), query.getOrderBy(), query.getColumns()));
    return builder.build();
  }
}
