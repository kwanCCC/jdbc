package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import java.sql.SQLException;


import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.GroupByQuery;
import org.doraemon.treasure.grammer.parser.Query;
import org.joda.time.Interval;

import org.doraemon.treasure.grammer.backends.checker.GroupByChecker;

public class GroupByQueryCompiler extends IMetricStoreQueryCompiler<GroupByQuery>
{

  @Override
  public boolean canCompile(Query query) throws SQLException
  {
    return GroupByChecker.isConditionMet(query);
  }

  @Override
  protected GroupByQuery buildConcreteQuery(Query query) throws SQLException
  {
    Interval interval = new Interval(query.getTimestamps().getLeft(), query.getTimestamps().getRight());
    GroupByQuery.GroupByQueryBuilder builder = GroupByQuery.builder();
    builder.type("groupby");
    builder.dataSource(query.getTable());
    builder.interval(interval);
    builder.columns(convertOperandsToColumns(query.getColumns()));
    builder.filter(convertBooleanExprToFilter(query.getWhereClause()));
    builder.groupBys(convertGroupBys(query.getGroupBys()));
    builder.limit(convertLimit(query.getLimit(), query.getOrderBy(), query.getColumns()));
    builder.points(buildPoints(interval, query.getGranularity()));
    return builder.build();
  }

}
