package org.doraemon.treasure.grammer.backends.druid.compiler;

import org.doraemon.treasure.grammer.backends.checker.TimeSeriesChecker;

import org.doraemon.treasure.grammer.backends.druid.dsl.queries.IQueryObject;
import org.doraemon.treasure.grammer.backends.druid.dsl.queries.TimeSeriesQuery;
import org.doraemon.treasure.grammer.parser.Query;

import java.sql.SQLException;

public class TimeSeriesCompiler extends IDruidQueryCompiler
{

  @Override
  public boolean canCompile(Query query) throws SQLException
  {
    return TimeSeriesChecker.isConditionMet(query);
  }

  @Override
  protected IQueryObject compile(CompilerContext context)
  {
    return TimeSeriesQuery.builder()
                          .dataSource(context.getDataSource())
                          .filter(context.getFilter())
                          .aggregations(context.getAggregators().keySet())
                          .postAggregations(context.getPostAggregators().keySet())
                          .granularity(context.getGranularity())
                          .intervals(context.getInterval())
                          .build();
  }

}
