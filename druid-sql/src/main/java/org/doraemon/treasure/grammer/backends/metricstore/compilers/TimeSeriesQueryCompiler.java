package org.doraemon.treasure.grammer.backends.metricstore.compilers;

import java.sql.SQLException;

import org.doraemon.treasure.grammer.backends.metricstore.dsl.queries.TimeSeriesQuery;
import org.doraemon.treasure.grammer.parser.Query;
import org.joda.time.Interval;

import org.doraemon.treasure.grammer.backends.checker.TimeSeriesChecker;

public class TimeSeriesQueryCompiler extends IMetricStoreQueryCompiler<TimeSeriesQuery>
{

  @Override
  public boolean canCompile(Query query) throws SQLException
  {
    return TimeSeriesChecker.isConditionMet(query);
  }

  @Override
  protected TimeSeriesQuery buildConcreteQuery(Query query) throws SQLException
  {
    Interval interval = new Interval(query.getTimestamps().getLeft(), query.getTimestamps().getRight());
    TimeSeriesQuery.TimeSeriesQueryBuilder builder = TimeSeriesQuery.builder();
    builder.type("timeseries");
    builder.dataSource(query.getTable());
    builder.interval(interval);
    builder.points(buildPoints(interval, query.getGranularity()));
    builder.columns(convertOperandsToColumns(query.getColumns()));
    builder.filter(convertBooleanExprToFilter(query.getWhereClause()));
    return builder.build();
  }

}
